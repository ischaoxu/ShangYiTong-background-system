package com.atguigu.syt.hosp.client.impl;

import com.atguigu.syt.hosp.client.ScheduleFeignClient;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 20:28
 */
@Component
public class ScheduleFeignClientImpl implements ScheduleFeignClient {
    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        return null;
    }
}
