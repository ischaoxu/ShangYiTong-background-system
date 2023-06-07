package com.atguigu.syt.hosp.mapper;

import com.atguigu.syt.model.hosp.Schedule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

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


}
