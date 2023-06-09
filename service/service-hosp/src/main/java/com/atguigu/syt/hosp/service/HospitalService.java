package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.Hospital;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
    /**
     *  保存医院信息
     * @author liuzhaoxu
     * @date 2023/6/7 19:21
     * @param paramMap
     */

    void save(Map<String, Object> paramMap,String hoscode);

    /**
     *  根据医院唯一标识获取医院信息
     * @author liuzhaoxu
     * @date 2023/6/7 20:44
     * @param hoscode
     * @return com.atguigu.syt.model.hosp.Hospital
     */

    Hospital findHospital(String hoscode);

    /**
     *  获取医院分页列表
     * @author liuzhaoxu
     * @date 2023/6/9 18:30
     * @param page
     * @param limit
     * @param hosname
     * @return org.springframework.data.domain.Page<com.atguigu.syt.model.hosp.Hospital>
     */

    Page<Hospital> pageHospital(Integer page, Integer limit, String hosname);

    /**
     *  修改医院状态
     * @author liuzhaoxu
     * @date 2023/6/9 19:36
     * @param hoscode
     * @param status
     */

    void updateStatus(String hoscode, Integer status);
}
