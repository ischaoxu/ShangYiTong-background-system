package com.atguigu.syt.hosp.receiver;

import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.rabbit.config.MQConst;
import com.atguigu.syt.vo.order.OrderMqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liuzhaoxu
 * @date 2023年06月20日 19:41
 */
@Component
@Slf4j
public class HospReceiver {
    @Resource
    private ScheduleService scheduleService;

    /**
     * 监听MQ中的消息
     * @param orderMqVo
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConst.QUEUE_ORDER, durable = "true"), //消息队列，并持久化
            exchange = @Exchange(value = MQConst.EXCHANGE_DIRECT_ORDER), //交换机
            key = {MQConst.ROUTING_ORDER} //路由
    ))
    public void receive(OrderMqVo orderMqVo){
        //修改排班信息
        log.info("HospReceiver 监听器监听到消息......");
        scheduleService.updateByOrderMqVo(orderMqVo);
    }
}
