package com.atguigu.syt.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.service.utils.HttpRequestHelper;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.enums.OrderStatusEnum;
import com.atguigu.syt.hosp.client.HospitalFeignClient;
import com.atguigu.syt.hosp.client.ScheduleFeignClient;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.order.mapper.OrderInfoMapper;
import com.atguigu.syt.order.service.OrderInfoService;
import com.atguigu.syt.order.service.WxPayService;
import com.atguigu.syt.rabbit.RabbitService;
import com.atguigu.syt.rabbit.config.MQConst;
import com.atguigu.syt.user.client.UserFeignClient;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import com.atguigu.syt.vo.order.OrderMqVo;
import com.atguigu.syt.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author com/atguigu
 * @since 2023-06-19
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;

    @Autowired
    private RabbitService rabbitService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private ScheduleFeignClient scheduleFeignClient;

    @Autowired
    private HospitalFeignClient hospitalFeignClient;

    @Autowired
    private WxPayService wxPayService;


    @Override
    public Long generateOrder(String scheduleId, Long patientId) {
//        1,获取就诊人信息
        Patient patient = userFeignClient.getPatient(patientId);
        if (patient == null) {
            throw new GuiguException(ResultCodeEnum.PARAM_ERROR);
        }
//        2,获取排班信息
        ScheduleOrderVo scheduleOrderVo = scheduleFeignClient.getScheduleOrderVo(scheduleId);
        if (scheduleOrderVo == null) {
            throw new GuiguException(ResultCodeEnum.PARAM_ERROR);
        }

//      3，获取请求秘钥
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(scheduleOrderVo.getHoscode());
        if (signInfoVo == null) {
            throw new GuiguException(ResultCodeEnum.PARAM_ERROR);
        }
//        4，判断是否有余号
        if (scheduleOrderVo.getAvailableNumber() <= 0) {
            throw new GuiguException(ResultCodeEnum.NUMBER_NO);
        }
        //创建订单对象
        OrderInfo orderInfo = new OrderInfo();

        //基本信息
        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        orderInfo.setOutTradeNo(outTradeNo); //订单号
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());//未支付

        //就诊人数据
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setPatientName(patient.getName());
        orderInfo.setUserId(patient.getUserId());

        //医院排班相关数据
        orderInfo.setScheduleId(scheduleId);
        BeanUtils.copyProperties(scheduleOrderVo, orderInfo);//拷贝相关属性
        //step4：调用医院端接口获取相关数据
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("hoscode", scheduleOrderVo.getHoscode());
        paramsMap.put("depcode", scheduleOrderVo.getDepcode());
        paramsMap.put("hosScheduleId", scheduleOrderVo.getHosScheduleId());
        paramsMap.put(
                "reserveDate",
                new DateTime(scheduleOrderVo.getReserveDate()).toString("yyyy-MM-dd")
        );
        paramsMap.put("reserveTime", scheduleOrderVo.getReserveTime());
        paramsMap.put("amount", scheduleOrderVo.getAmount());
        paramsMap.put("name", patient.getName());
        paramsMap.put("certificatesType", patient.getCertificatesType());
        paramsMap.put("certificatesNo", patient.getCertificatesNo());
        paramsMap.put("sex", patient.getSex());
        paramsMap.put("birthdate", patient.getBirthdate());
        paramsMap.put("phone", patient.getPhone());
        paramsMap.put("isMarry", patient.getIsMarry());
        paramsMap.put("timestamp", HttpRequestHelper.getTimestamp());

        paramsMap.put("sign", HttpRequestHelper.getSign(paramsMap, signInfoVo.getSignKey()));//标准签名
        JSONObject jsonResult = HttpRequestHelper.sendRequest(
                paramsMap, signInfoVo.getApiUrl() + "/order/submitOrder"
        );
        log.info("结果：" + jsonResult.toJSONString());

        if (jsonResult.getInteger("code") != 200) {//失败

            log.error("预约失败，"
                    + "code：" + jsonResult.getInteger("code")
                    + "，message：" + jsonResult.getString("message")
            );
            throw new GuiguException(ResultCodeEnum.FAIL.getCode(), jsonResult.getString("message"));
        }

        JSONObject data = jsonResult.getJSONObject("data");
        String hosOrderId = data.getString("hosOrderId");
        Integer number = data.getInteger("number");
        String fetchTime = data.getString("fetchTime");
        String fetchAddress = data.getString("fetchAddress");
        orderInfo.setHosOrderId(hosOrderId);
        orderInfo.setNumber(number);
        orderInfo.setFetchTime(fetchTime);
        orderInfo.setFetchAddress(fetchAddress);
        baseMapper.insert(orderInfo);

        //使用这两个数据更新平台端的最新的排班数量
        Integer reservedNumber = data.getInteger("reservedNumber");
        Integer availableNumber = data.getInteger("availableNumber");

        //目的1：更新mongodb数据库中的排班数量
//        value = @Queue(value = MQConst.QUEUE_ORDER, durable = "true"), //消息队列，并持久化
//        exchange = @Exchange(value = MQConst.EXCHANGE_DIRECT_ORDER), //交换机
//        key = {MQConst.ROUTING_ORDER} //路由
        // 中间件：MQ 异步解耦
        OrderMqVo orderMqVo = new OrderMqVo();
        orderMqVo.setReservedNumber(reservedNumber);
        orderMqVo.setAvailableNumber(availableNumber);
        orderMqVo.setScheduleId(scheduleId);
        rabbitService.sendMessage(
                MQConst.EXCHANGE_DIRECT_ORDER,
                MQConst.ROUTING_ORDER,
                orderMqVo
                );



        //目的2：给就诊人发短信 TODO：MQ

        //返回订单id
        return orderInfo.getId();
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        return packOrderInfo(orderInfo);
    }

    @Override
    public List<OrderInfo> selectList(Long userId) {
        List<OrderInfo> orderInfoList = baseMapper.selectList(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getUserId, userId));
        orderInfoList.forEach(this::packOrderInfo);
        return orderInfoList;
    }

    @Override
    public OrderInfo getByOutTradeNo(String outTradeNo) {
        return baseMapper.selectOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOutTradeNo, outTradeNo));
    }

    @Override
    public void updateStatus(String outTradeNo, Integer status) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(status);
        baseMapper.update(orderInfo, new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOutTradeNo, outTradeNo));
    }

    @Override
    public void cancelOrder(String outTradeNo) {
//        获取订单信息
        OrderInfo orderInfo = baseMapper.selectOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOutTradeNo, outTradeNo));
//        提前获取秘钥
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode()).getSignKey();
        }, executor);
        //当前时间大于退号时间，不能取消预约
        DateTime quitTime = new DateTime(orderInfo.getQuitTime());
        if (quitTime.isBeforeNow()) {
            throw new GuiguException(ResultCodeEnum.CANCEL_ORDER_NO);
        }
//        判断订单状态
        int status = orderInfo.getOrderStatus().intValue();
        if (status == OrderStatusEnum.PAID.getStatus()) {
//   退款
            wxPayService.refund(outTradeNo);
//            修改状态
            orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
            baseMapper.updateById(orderInfo);
        } else {
//            修改状态
            orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
            baseMapper.updateById(orderInfo);
        }
        //调用医院端接口，同步数据
        Map<String, Object> params = new HashMap<>();
        params.put("hoscode", orderInfo.getHoscode());
        params.put("hosOrderId", orderInfo.getHosOrderId());
        params.put("hosScheduleId", orderInfo.getHosScheduleId());
        params.put("timestamp", HttpRequestHelper.getTimestamp());
        params.put("sign", HttpRequestHelper.getSign(params, future.join()));
        JSONObject jsonResult = HttpRequestHelper.sendRequest(params, "http://localhost:9998/order/updateCancelStatus");
        if (jsonResult.getInteger("code") != 200) {
            throw new GuiguException(ResultCodeEnum.CANCEL_ORDER_FAIL);
        }


    }

    /**
     * 封装订单数据
     *
     * @param orderInfo
     * @return
     */
    private OrderInfo packOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put(
                "orderStatusString",
                OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus())
        );
        return orderInfo;
    }

}
