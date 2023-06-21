//package com.atguigu.syt.yun.receiver;
//
//import com.atguigu.syt.rabbit.config.MQConst;
//import com.atguigu.syt.vo.sms.SmsVo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author liuzhaoxu
// * @date 2023年06月20日 19:38
// */
//
//@Component
//@Slf4j
//public class SmsReceiver {
//
//    @Resource
//    private SmsService smsService;
//
//    /**
//     * 监听MQ中的消息
//     * @param smsVo
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = MQConst.QUEUE_SMS, durable = "true"), //消息队列，并持久化
//            exchange = @Exchange(value = MQConst.EXCHANGE_DIRECT_SMS), //交换机
//            key = {MQConst.ROUTING_SMS} //路由
//    ))
//    public void receive(SmsVo smsVo){
//        log.info("SmsReceiver 监听器监听到消息......");
//        smsService.send(smsVo);
//    }
//}