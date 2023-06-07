package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.Hospital;

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
}
