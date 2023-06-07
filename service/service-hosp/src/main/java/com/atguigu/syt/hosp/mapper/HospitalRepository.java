package com.atguigu.syt.hosp.mapper;


import com.atguigu.syt.model.hosp.Hospital;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface HospitalRepository extends MongoRepository<Hospital, ObjectId> {
    /**
     *  根据医院唯一标识查找医院信息
     * @author liuzhaoxu
     * @date 2023/6/7 19:25
     * @param hoscode
     * @return com.atguigu.syt.model.hosp.Hospital
     */

    Hospital findByHoscode(String hoscode);
}
