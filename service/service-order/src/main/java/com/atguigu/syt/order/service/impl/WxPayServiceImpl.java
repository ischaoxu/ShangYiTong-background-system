package com.atguigu.syt.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.service.utils.HttpRequestHelper;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.enums.OrderStatusEnum;
import com.atguigu.syt.hosp.client.HospitalFeignClient;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.order.config.WxpayProperties;
import com.atguigu.syt.order.service.OrderInfoService;
import com.atguigu.syt.order.service.PaymentInfoService;
import com.atguigu.syt.order.service.RefundInfoService;
import com.atguigu.syt.order.service.WxPayService;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author liuzhaoxu
 * @date 2023年06月20日 11:19
 */
@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;
    @Autowired
    private WxpayProperties wxpayProperties;
    @Autowired
    private RSAAutoCertificateConfig rsaAutoCertificateConfig;
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RefundInfoService refundInfoService;

    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private HospitalFeignClient hospitalFeignClient;

    @Override
    public String createNative(String outTradeNo) {
        // 构建service
        NativePayService service = new NativePayService.Builder().config(rsaAutoCertificateConfig).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        //获取订单
        OrderInfo orderInfo = orderInfoService.getByOutTradeNo(outTradeNo);

        PrepayRequest request = new PrepayRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        request.setAppid(wxpayProperties.getAppid());
        request.setMchid(wxpayProperties.getMchId());

        request.setDescription(orderInfo.getTitle());
        request.setOutTradeNo(outTradeNo);
        request.setNotifyUrl(wxpayProperties.getNotifyUrl());

        Amount amount = new Amount();
        //amount.setTotal(orderInfo.getAmount().multiply(new BigDecimal(100)).intValue());
        amount.setTotal(1);//1分钱
        request.setAmount(amount);
        // 调用接口
        PrepayResponse prepayResponse = service.prepay(request);

        return prepayResponse.getCodeUrl();

    }

    @Override
    public boolean queryPayStatus(String outTradeNo) {

        OrderInfo orderInfo = orderInfoService.getByOutTradeNo(outTradeNo);

        if (orderInfo.getOrderStatus().intValue() != OrderStatusEnum.UNPAID.getStatus()) {
            return true;
        }
//        提前获取密钥
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode()).getSignKey();
        }, executor);

        try {
            // 构建service
            NativePayService service = new NativePayService.Builder().config(rsaAutoCertificateConfig).build();

//        创建查询对象
            QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
            request.setMchid(wxpayProperties.getMchId());
            request.setOutTradeNo(outTradeNo);

//获取返回信息
            Transaction transaction = service.queryOrderByOutTradeNo(request);

            Transaction.TradeStateEnum tradeState = transaction.getTradeState();

            if (tradeState.equals(Transaction.TradeStateEnum.SUCCESS)) {
                //通知医院修改订单状态
                //组装参数
                HashMap<String, Object> paramsMap = new HashMap<>();
                CompletableFuture.runAsync(() -> {
                    paramsMap.put("hoscode", orderInfo.getHoscode());
                    paramsMap.put("hosOrderId", orderInfo.getHosOrderId());
                    paramsMap.put("timestamp", HttpRequestHelper.getTimestamp());
                    paramsMap.put("sign", HttpRequestHelper.getSign(paramsMap, future.join()));
                    //发送请求
                    JSONObject jsonResult = HttpRequestHelper.sendRequest(paramsMap, "http://localhost:9998/order/updatePayStatus");
                    //解析响应
                    if (jsonResult.getInteger("code") != 200) {
                        log.error("查单失败，"
                                + "code：" + jsonResult.getInteger("code")
                                + "，message：" + jsonResult.getString("message")
                        );
                        throw new GuiguException(ResultCodeEnum.FAIL.getCode(), jsonResult.getString("message"));
                    }
                }, executor);
                //更新订单状态
                orderInfoService.updateStatus(outTradeNo, OrderStatusEnum.PAID.getStatus());
                //记录支付日志
                paymentInfoService.savePaymentInfo(orderInfo, transaction);
                //返回支付结果
                return true;
            }
            return false;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.error(e.getHttpRequest().toString());
            throw new GuiguException(ResultCodeEnum.FAIL);
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.error(e.getResponseBody());
            throw new GuiguException(ResultCodeEnum.FAIL);
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.error(e.getMessage());
            throw new GuiguException(ResultCodeEnum.FAIL);
        }
    }

    @Override
    public void refund(String outTradeNo) {
        //获取订单
        OrderInfo orderInfo = orderInfoService.getByOutTradeNo(outTradeNo);
        // 初始化服务
        RefundService service = new RefundService.Builder().config(rsaAutoCertificateConfig).build();
        // ... 调用接口
        try {
            CreateRequest request = new CreateRequest();
            // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
            request.setOutTradeNo(outTradeNo);
            request.setOutRefundNo("TK_" + outTradeNo);
            AmountReq amount = new AmountReq();
            //amount.setTotal(orderInfo.getAmount().multiply(new BigDecimal(100)).intValue());
            amount.setTotal(1L);//1分钱
            amount.setRefund(1L);
            amount.setCurrency("CNY");
            request.setAmount(amount);
            // 调用接口
            Refund response = service.create(request);
/*status	string[1, 32]	是	退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台-交易中心，手动处理此笔退款。
枚举值：
SUCCESS：退款成功
CLOSED：退款关闭
PROCESSING：退款处理中
ABNORMAL：退款异常
示例值：SUCCESS
*/
            Status status = response.getStatus();
            if(Status.CLOSED.equals(status)){

                throw new GuiguException(ResultCodeEnum.FAIL.getCode(), "退款已关闭，无法退款");

            }else if(Status.ABNORMAL.equals(status)){

                throw new GuiguException(ResultCodeEnum.FAIL.getCode(), "退款异常");

            } else{
                //SUCCESS：退款成功（退款申请成功） || PROCESSING：退款处理中
                //记录支退款日志
                refundInfoService.saveRefundInfo(orderInfo, response);
            }
        } catch (HttpException e) { // 发送HTTP请求失败
            log.info("退款=》 发送HTTP请求失败");
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            log.info("退款=》 服务返回状态小于200或大于等于300");

            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            log.info("退款=》 服务返回成功，返回体类型不合法");
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
        }
    }


//    CreateRequest request = new CreateRequest();
}
