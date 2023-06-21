package com.atguigu.syt.order.service;

import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.model.order.RefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wechat.pay.java.service.refund.model.Refund;

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
}
