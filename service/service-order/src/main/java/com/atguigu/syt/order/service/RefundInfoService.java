package com.atguigu.syt.order.service;

import com.atguigu.syt.enums.RefundStatusEnum;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.order.RefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;

/**
 * <p>
 * 退款信息表 服务类
 * </p>
 *
 * @author com/atguigu
 * @since 2023-06-19
 */
public interface RefundInfoService extends IService<RefundInfo> {

    /**
     *  保持退款信息
     * @author liuzhaoxu
     * @date 2023/6/20 18:42
     * @param orderInfo
     * @param response
     */

    void saveRefundInfo(OrderInfo orderInfo, Refund response);

    /**
     *  更新退款状态
     * @author liuzhaoxu
     * @date 2023/6/21 13:54
     * @param refundNotification
     * @param refund
     */

    void updateRefundInfoStatus(RefundNotification refundNotification, RefundStatusEnum refund);
}
