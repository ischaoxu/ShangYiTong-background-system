package com.atguigu.syt.rabbit.config;

/**
 * @author liuzhaoxu
 * @date 2023年06月20日 19:36
 */
public class MQConst {

    /**
     * 预约/取消订单
     */
    public static final String EXCHANGE_DIRECT_ORDER = "exchange.direct.order";
    public static final String ROUTING_ORDER = "routing.order";
    public static final String QUEUE_ORDER  = "queue.order";

    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_SMS = "exchange.direct.sms";
    public static final String ROUTING_SMS = "routing.sms";
    public static final String QUEUE_SMS  = "queue.sms";
}