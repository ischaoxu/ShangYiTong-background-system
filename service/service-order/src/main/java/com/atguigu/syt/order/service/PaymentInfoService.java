package com.atguigu.syt.order.service;

import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wechat.pay.java.service.payments.model.Transaction;

/**
 * <p>
 * 支付信息表 服务类
 * </p>
 *
 * @author com/atguigu
 * @since 2023-06-19
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     *  保存支付记录
     * @author liuzhaoxu
     * @@date 2023/6/20 17:06
     * @param orderInfo
     * @param transaction
     */
    void savePaymentInfo(OrderInfo orderInfo, Transaction transaction);

}
