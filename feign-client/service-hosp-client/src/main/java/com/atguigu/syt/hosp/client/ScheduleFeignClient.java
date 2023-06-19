package com.atguigu.syt.hosp.client;

import com.atguigu.syt.hosp.client.impl.ScheduleFeignClientImpl;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 20:28
 */
@FeignClient(
        value = "service-hosp",
        contextId = "scheduleFeignClient",
        fallback = ScheduleFeignClientImpl.class
)
public interface ScheduleFeignClient {
    @GetMapping("/inner/hosp/hospital/getScheduleOrderVo/{scheduleId}")
    ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId);
}
