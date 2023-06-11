package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.mapper.ScheduleRepository;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.hosp.util.DateUtil;
import com.atguigu.syt.model.hosp.Schedule;
import com.atguigu.syt.vo.hosp.ScheduleRuleVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月07日 20:30
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
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

    @Override
    public Map<String, Object> getSchedulePage(Long page, Long limit, String hoscode, String depcode) {
        Schedule schedule = new Schedule();
        schedule.setHoscode(hoscode);
        schedule.setDepcode(depcode);
        Criteria criteria = Criteria.byExample(Example.of(schedule));
        Aggregation aggregation = Aggregation.newAggregation(
//                缩小范围
                Aggregation.match(criteria),
//                分组条件
//                聚合
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
//                排序
                Aggregation.sort(Sort.by(Sort.Direction.ASC,"workDate")),
//                分页
                Aggregation.skip(page-1),
                Aggregation.limit(limit)
        );
        AggregationResults<ScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, ScheduleRuleVo.class);
//        查询结果集合
        List<ScheduleRuleVo> list = aggregate.getMappedResults();
//      翻译日期为周
        list.forEach(scheduleRuleVo -> {
            String ofWeek = DateUtil.getDayOfWeek(new DateTime(scheduleRuleVo.getWorkDate()));
            scheduleRuleVo.setDayOfWeek(ofWeek);
        });
//        查询分组后的总条数
        Aggregation totalAgg = Aggregation.newAggregation( Aggregation.match(criteria));
        int total = mongoTemplate.aggregate(totalAgg, Schedule.class, ScheduleRuleVo.class).getMappedResults().size();
        HashMap<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("total", total);
        return map;
    }

    @Override
    public List<Schedule> listSchedule(String hoscode, String depcode, String workDate) {
        List<Schedule>  scheduleList = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,DateTime.parse(workDate));
        return scheduleList;
    }
}
