package com.atguigu.syt.hosp.mapper;


import com.atguigu.syt.model.hosp.Hospital;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    /**
     *  根据名查询分页
     * @author liuzhaoxu
     * @date 2023/6/9 18:46
     * @param hosname
     * @param pageRequest
     * @return org.springframework.data.domain.Page<com.atguigu.syt.model.hosp.Hospital>
     */

    Page<Hospital> findByHosnameLike(String hosname, PageRequest pageRequest);


    /**
     *  根据医院hoscode查询医院详细信息
     * @author liuzhaoxu
     * @date 2023/6/10 8:39
     * @param hoscode
     * @return com.atguigu.syt.model.hosp.Hospital
     */

    Hospital getByHoscode(String hoscode);
}
