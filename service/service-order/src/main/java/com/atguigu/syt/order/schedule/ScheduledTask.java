package com.atguigu.syt.order.schedule;

import com.atguigu.syt.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaoxu
 * @date 2023年06月25日 17:47
 */
@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {

    @Autowired
    private OrderInfoService orderInfoService;
    @Scheduled(cron = "0 0 18 * * ? ")
    public void taskPatientAdvice() {
        log.info("定时短信通知就诊人");
//        orderInfoService.patientAdvice();
    }

}
