package com.atguigu.syt.order.service;

public interface WxPayService {
    /**
     *  创建支付二维码
     * @author liuzhaoxu
     * @date 2023/6/20 11:20
     * @param outTradeNo
     * @return java.lang.String
     */

    String createNative(String outTradeNo);

    /**
     *  获取支付状态
     * @author liuzhaoxu
     * @date 2023/6/20 16:36
     * @param outTradeNo
     * @return boolean
     */

    boolean queryPayStatus(String outTradeNo);
    /**
     * 退款
     * @param outTradeNo
     */
    void refund(String outTradeNo);

}
