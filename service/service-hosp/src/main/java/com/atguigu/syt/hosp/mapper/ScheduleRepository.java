package com.atguigu.syt.hosp.mapper;

import com.atguigu.syt.model.hosp.Schedule;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, ObjectId> {
    /**
     *  根据医院标识,排班编号获取排班信息
     * @author liuzhaoxu
     * @date 2023/6/7 20:34
     * @param hoscode
     * @param hosScheduleId
     * @return com.atguigu.syt.model.hosp.Schedule
     */

    Schedule findByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);


    /**
     *  根据医院id科室id获取科室排班信息
     * @author liuzhaoxu
     * @date 2023/6/11 18:04
     * @param hoscode
     * @param depcode
     * @return java.util.List<com.atguigu.syt.model.hosp.Schedule>
     */
    List<Schedule> findByHoscodeAndDepcode(String hoscode, String depcode);

    /**
     *  根据医院,科室,日期获取当天排班信息
     * @author liuzhaoxu
     * @date 2023/6/11 19:36
     * @param hoscode
     * @param depcode
     * @param parse
     * @return java.util.List<com.atguigu.syt.model.hosp.Schedule>
     */

    List<Schedule> findByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, DateTime parse);
}
