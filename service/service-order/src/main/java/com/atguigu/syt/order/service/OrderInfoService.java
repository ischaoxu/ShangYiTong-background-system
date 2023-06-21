package com.atguigu.syt.order.service;

import com.atguigu.syt.model.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author com/atguigu
 * @since 2023-06-19
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     *  生成订单
     * @author liuzhaoxu
     * @date 2023/6/19 20:01
     * @param scheduleId
     * @param patientId
     * @return java.lang.Long
     */

    Long generateOrder(String scheduleId, Long patientId);

    /**
     *  获取订单详情
     * @author liuzhaoxu
     * @date 2023/6/19 21:11
     * @param orderId
     * @return com.atguigu.syt.model.order.OrderInfo
     */

    OrderInfo getOrderInfo(Long orderId);

    /**
     *  获取订单列表
     * @author liuzhaoxu
     * @date 2023/6/19 21:13
     * @param userId
     * @return java.util.List<com.atguigu.syt.model.order.OrderInfo>
     */

    List<OrderInfo> selectList(Long userId);

    /**
     *  根据出纳单号获取订单信息
     * @author liuzhaoxu
     * @date 2023/6/20 13:05
     * @param outTradeNo
     * @return com.atguigu.syt.model.order.OrderInfo
     */

    OrderInfo getByOutTradeNo(String outTradeNo);

    /**
     *  更新订单状态
     * @author liuzhaoxu
     * @date 2023/6/20 17:04
     * @param outTradeNo
     * @param status
     */

    void updateStatus(String outTradeNo, Integer status);

    /**
     *  取消预约
     * @author liuzhaoxu
     * @date 2023/6/20 18:07
 * @param outTradeNo
     */

    void cancelOrder(String outTradeNo);
}
