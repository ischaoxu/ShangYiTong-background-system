package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.mapper.HospitalRepository;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月07日 19:19
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> paramMap,String hoscode) {
        Hospital newHospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Hospital.class);
       Hospital oldHospital =  hospitalRepository.findByHoscode(hoscode);
        if (oldHospital != null) {
            newHospital.setId(oldHospital.getId());
            newHospital.setStatus(oldHospital.getStatus());
            hospitalRepository.save(newHospital);
        } else {
            hospitalRepository.save(newHospital);
        }
    }

    @Override
    public Hospital findHospital(String hoscode) {
        return hospitalRepository.findByHoscode(hoscode);
    }
}
