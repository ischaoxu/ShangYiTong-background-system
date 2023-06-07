package com.atguigu.syt.hosp.mapper;

import com.atguigu.syt.model.hosp.Department;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface DepartmentRepository extends MongoRepository<Department, ObjectId> {
    /**
     *  根据医院唯一标识和科室编号查找科室信息
     * @author liuzhaoxu
     * @date 2023/6/7 20:18
     * @param hoscode
     * @param depcode
     * @return com.atguigu.syt.model.hosp.Department
     */

    Department findByHoscodeAndDepcode(String hoscode, String depcode);

    /**
     *  根据医院标识查询所有科室信息
     * @author liuzhaoxu
     * @date 2023/6/7 20:51
     * @param hoscode
     * @return java.util.List<com.atguigu.syt.model.hosp.Department>
     */

    List<Department> findByHoscode(String hoscode);
}
