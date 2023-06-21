package com.atguigu.syt.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liuzhaoxu
 * @date 2023年06月20日 19:34
 */
@Service
@Slf4j
public class RabbitService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     *  发送消息
     * @param exchange 交换机
     * @param routingKey 路由
     * @param message 消息
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        log.info("发送消息==>"+message.toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }
}