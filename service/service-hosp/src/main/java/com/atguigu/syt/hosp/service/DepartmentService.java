package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.Department;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService {
    /**
     *  上传科室信息
     * @author liuzhaoxu
     * @date 2023/6/7 20:14
     * @param paramMap
     * @param hoscode
     * @param depcode
     */

    void save(Map<String, Object> paramMap, String hoscode, String depcode);
    /**
     *  根据医院唯一标识获取所有科室列表
     * @author liuzhaoxu
     * @date 2023/6/7 20:54
     * @param page
     * @param limit
     * @param hoscode
     * @return org.springframework.data.domain.Page<com.atguigu.syt.model.hosp.Department>
     */

    Page<Department> getDepartmentList(int page, int limit, String hoscode);

    /**
     *  删除科室
     * @author liuzhaoxu
     * @date 2023/6/8 10:22
     * @param paramMap
     */
    void deleteDepartment(Map<String, Object> paramMap);


}
