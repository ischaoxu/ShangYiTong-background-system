package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.mapper.ScheduleRepository;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月07日 20:30
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Override
    public void save(Map<String, Object> paramMap, String hoscode, String hosScheduleId) {
//        获取新的排班信息
        Schedule newSchedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);
        //        获取旧的排班信息
        Schedule schedule =scheduleRepository.findByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
        if (schedule == null) {
            scheduleRepository.save(newSchedule);
        }else {
            newSchedule.setId(schedule.getId());
            scheduleRepository.save(newSchedule);
        }


    }

    @Override
    public Page<Schedule> listSchedulePage(Integer page, Integer limit, String hoscode) {
        Sort sort = Sort.by(Sort.Direction.ASC, "workDate");
        Schedule schedule = new Schedule();
        schedule.setHoscode(hoscode);
        Example<Schedule> example = Example.of(schedule);
        return  scheduleRepository.findAll(example, PageRequest.of(page - 1, limit, sort));
    }

    @Override
    public void deleteSchedule(Map<String, Object> paramMap) {
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        Schedule schedule = scheduleRepository.findByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }
}
