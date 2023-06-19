package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.mapper.DepartmentRepository;
import com.atguigu.syt.hosp.mapper.HospitalRepository;
import com.atguigu.syt.hosp.mapper.ScheduleRepository;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.hosp.util.DateUtil;
import com.atguigu.syt.model.hosp.BookingRule;
import com.atguigu.syt.model.hosp.Department;
import com.atguigu.syt.model.hosp.Hospital;
import com.atguigu.syt.model.hosp.Schedule;
import com.atguigu.syt.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import com.atguigu.syt.vo.hosp.ScheduleRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuzhaoxu
 * @date 2023年06月07日 20:30
 */
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Map<String, Object> paramMap, String hoscode, String hosScheduleId) {
//        获取新的排班信息
        Schedule newSchedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);
        //        获取旧的排班信息
        Schedule schedule = scheduleRepository.findByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule == null) {
            scheduleRepository.save(newSchedule);
        } else {
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
        return scheduleRepository.findAll(example, PageRequest.of(page - 1, limit, sort));
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
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "workDate")),
//                分页
                Aggregation.skip(page - 1),
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
        Aggregation totalAgg = Aggregation.newAggregation(Aggregation.match(criteria));
        int total = mongoTemplate.aggregate(totalAgg, Schedule.class, ScheduleRuleVo.class).getMappedResults().size();
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public List<Schedule> listSchedule(String hoscode, String depcode, String workDate) {
        List<Schedule> scheduleList = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, DateTime.parse(workDate).toDate());
        scheduleList.forEach(schedule -> schedule.getParam().put("id", schedule.getId().toString()));
        return scheduleList;
    }

    @Override
    public Map<String, Object> getScheduleCycleInfo(String hoscode, String depcode) {

        //1 根据hoscode查询医院信息，取出预约规则
        Hospital hospital = hospitalService.getHospital(hoscode);
        BookingRule bookingRule = hospital.getBookingRule();
        //2 根据预约规则推算出可以预约的日期集合List<Date>
        List<Date> dateList = this.getDateList(bookingRule);
        //3 参考后台接口，实现聚合查询List<BookingScheduleRuleVo>
        //3.1创建查询条件对象
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode).and("workDate").in(dateList);
        //3.2创建聚合查询对象，定义查询语句
        Aggregation agg = Aggregation.newAggregation(
                //3.2.1设置筛选条件
                Aggregation.match(criteria),
                //3.2.2指定分组字段，统计字段
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .sum("availableNumber").as("availableNumber")
        );
        //3.3执行聚合查询，获取结果
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleRuleVoList = aggregate.getMappedResults();
        //3.4实现类型转化List<BookingScheduleRuleVo>=》Map（k：workDate，v：BookingScheduleRuleVo）
        Map<Date,BookingScheduleRuleVo> scheduleRuleVoMap = new HashMap<>();

        if(!CollectionUtils.isEmpty(scheduleRuleVoList)){
            scheduleRuleVoMap = scheduleRuleVoList.stream().collect(Collectors.toMap(
                    bookingScheduleRuleVo -> bookingScheduleRuleVo.getWorkDate(),
                    bookingScheduleRuleVo-> bookingScheduleRuleVo
            ));
        }

        //4 整合步骤2 dateList、步骤3数据scheduleRuleVoMap
        //4.1 创建整合后list集合对象
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        int size = dateList.size();
        //4.2 遍历dateList集合，取出每一天日期date
        for (int i = 0; i <size ; i++) {
            Date date = dateList.get(i);
            //4.3 根据date从scheduleRuleVoMap里取出数据
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleRuleVoMap.get(date);
            //4.4如果数据为空，初始化对象BookingScheduleRuleVo
            if(bookingScheduleRuleVo==null){
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                bookingScheduleRuleVo.setWorkDate(date);
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            //4.5补全日期信息、周几信息
            bookingScheduleRuleVo.setWorkDateMd(date);
            bookingScheduleRuleVo.setDayOfWeek(DateUtil.getDayOfWeek(new DateTime(date)));

            //4.6根据数据判断状态值status状态  0：正常，  1：即将放号，  -1：当天已停止挂号
            //4.6.1最后一条记录，状态值为 1：即将放号，
            if(i==size-1){
                bookingScheduleRuleVo.setStatus(1);
            }else{
                bookingScheduleRuleVo.setStatus(0);
            }
            //4.6.2第一条记录，判断是否已过当天停止挂号时间，已过状态-1：当天已停止挂号
            if(i==0){
                //当天停止挂号时间
                DateTime stopDateTime = this.getDateTime(new DateTime(), bookingRule.getStopTime());
                if(stopDateTime.isBeforeNow()){
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            //4.7对象封装到list集合
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }
        //5 封装数据返回
//医院基本信息
        Map<String, String> info = new HashMap<>();
        //医院名称
        info.put("hosname", hospital.getHosname());
        //科室
        Department department = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);
        //大科室名称
        info.put("bigname", department.getBigname());
        //科室名称
        info.put("depname", department.getDepname());
        //当前月份
        info.put("workDateString", new DateTime().toString("yyyy年MM月"));
        //放号时间
        info.put("releaseTime", bookingRule.getReleaseTime());
        Map<String, Object> result = new HashMap<>();
        //可预约日期数据
        result.put("bookingScheduleList", bookingScheduleRuleVoList);//排班日期列表
        result.put("info", info);//医院基本信息
        return result;
    }

    @Override
    public Schedule getDetailById(String id) {
        Schedule schedule = scheduleRepository.findById(new ObjectId(id)).get();

        return packSchedule(schedule);
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        Schedule schedule = getDetailById(scheduleId);
        String hosname = (String)schedule.getParam().get("hosname");
        String depname = (String)schedule.getParam().get("depname");

        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        scheduleOrderVo.setHoscode(schedule.getHoscode()); //医院编号
        scheduleOrderVo.setHosname(hosname); //医院名称
        scheduleOrderVo.setDepcode(schedule.getDepcode()); //科室编号
        scheduleOrderVo.setDepname(depname); //科室名称
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId()); //医院端的排班主键
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber()); //剩余预约数
        scheduleOrderVo.setTitle(hosname + depname + "挂号费");
        scheduleOrderVo.setReserveDate(schedule.getWorkDate()); //安排日期
        scheduleOrderVo.setReserveTime(schedule.getWorkTime()); //安排时间（0：上午 1：下午）
        scheduleOrderVo.setAmount(schedule.getAmount());//挂号费用


        //获取预约规则相关数据
        Hospital hospital = hospitalRepository.findByHoscode(schedule.getHoscode());
        BookingRule bookingRule = hospital.getBookingRule();
        String quitTime = bookingRule.getQuitTime();//退号时间
        //退号实际时间（如：就诊前一天为-1，当天为0）
        DateTime quitDay = new DateTime(schedule.getWorkDate()).plusDays(bookingRule.getQuitDay());//退号日期
        DateTime quitDateTime = this.getDateTime(quitDay, quitTime);//可退号的具体的日期和时间
        scheduleOrderVo.setQuitTime(quitDateTime.toDate());

        return scheduleOrderVo;
    }

    //根据预约规则推算出可以预约的日期集合List<Date>
    private List<Date> getDateList(BookingRule bookingRule) {
        //1获取开始挂号时间（当前系统日期+挂号时间）
        DateTime releaseDateTime = this.getDateTime(new DateTime(), bookingRule.getReleaseTime());
        //2获取周期，判断是否已过放号时间，已过周期+1
        Integer cycle = bookingRule.getCycle();
        if(releaseDateTime.isBeforeNow()){
            cycle=cycle+1;
        }
        //3根据周期推算可以挂号日期，存入集合
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i <cycle ; i++) {
            DateTime plusDays = new DateTime().plusDays(i);
            String plusDaysStr = plusDays.toString("yyyy-MM-dd");
            dateList.add(new DateTime(plusDaysStr).toDate());
        }
        //4返回数据
        return dateList;
    }

    /**
     * 根据日期对象和时间字符串获取一个日期时间对象
     * @param dateTime
     * @param timeString
     * @return
     */
    private DateTime getDateTime(DateTime dateTime, String timeString) {
        String dateTimeString = dateTime.toString("yyyy-MM-dd") + " " + timeString;
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
    }


    /**
     * 封装医院名称，科室名称和周几
     *
     * @param schedule
     * @return
     */
    private Schedule packSchedule(Schedule schedule) {
        //医院名称
        String hosname = hospitalRepository.findByHoscode(schedule.getHoscode()).getHosname();
        //科室名称
        String depname = departmentRepository.findByHoscodeAndDepcode(schedule.getHoscode(), schedule.getDepcode()).getDepname();
        //周几
        String dayOfWeek = DateUtil.getDayOfWeek(new DateTime(schedule.getWorkDate()));

        Integer workTime = schedule.getWorkTime();
        String workTimeString = workTime.intValue() == 0 ? "上午" : "下午";

        schedule.getParam().put("hosname", hosname);
        schedule.getParam().put("depname", depname);
        schedule.getParam().put("dayOfWeek", dayOfWeek);
        schedule.getParam().put("workTimeString", workTimeString);

        //id为ObjectId类型时需要进行转换
        schedule.getParam().put("id", schedule.getId().toString());
        return schedule;
    }

}
