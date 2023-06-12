package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.cmn.client.DictFeignClient;
import com.atguigu.syt.cmn.client.RegionFeignClient;
import com.atguigu.syt.enums.DictTypeEnum;
import com.atguigu.syt.hosp.mapper.HospitalRepository;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月07日 19:19
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;
    @Autowired
    private RegionFeignClient regionFeignClient;

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

    @Override
    public Page<Hospital> pageHospital(Integer page, Integer limit, String hosname) {
        Sort sort = Sort.by(Sort.Direction.ASC, "hoscode");
        PageRequest pageRequest = PageRequest.of(page-1, limit, sort);
        Page<Hospital> hospitalPage = null;
        if (StringUtils.isEmpty(hosname)) {
            hospitalPage = hospitalRepository.findAll(pageRequest);
        } else {
            hospitalPage = hospitalRepository.findByHosnameLike(hosname,pageRequest);
        }
        List<Hospital> hospitalList = hospitalPage.getContent();
        hospitalList.forEach(hospital -> {
            this.packHospital(hospital);
        });
        return hospitalPage;
    }

    @Override
    public void updateStatus(String hoscode, Integer status) {
        if (status == 0 || status == 1) {
            Hospital hospital = hospitalRepository.findByHoscode(hoscode);
            hospital.setStatus(status);
            hospitalRepository.save(hospital);
            return;
        }
        throw  new GuiguException(ResultCodeEnum.PARAM_ERROR);
    }

    @Override
    public Hospital getHospital(String hoscode) {
       return hospitalRepository.getByHoscode(hoscode);
    }

    @Override
    public List<Hospital> listHospitalSearch(String hosname, String hostype, String districtCode) {
//        排序条件
        Sort sort = Sort.by(Sort.Direction.ASC, "hoscode");
//        查询模板
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("hosname", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("hostype", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("districtCode", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact());
//        查询条件
        Hospital hospital = new Hospital();
        hospital.setHosname(hosname);
        hospital.setHostype(hostype);
        hospital.setDistrictCode(districtCode);
        hospital.setStatus(1);
//        查询结果
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        List<Hospital> hospitalList = hospitalRepository.findAll(example, sort);
//         字典翻译
        hospitalList.forEach(this::packHospital);

        return hospitalList;
    }

    private Hospital packHospital(Hospital hospital) {
        String hostypeString = dictFeignClient.getName(DictTypeEnum.HOSTYPE.getDictTypeId(), hospital.getHostype());
        String provinceString = regionFeignClient.getName(hospital.getProvinceCode());
        String cityString = regionFeignClient.getName(hospital.getCityCode());
        if(provinceString.equals(cityString)) {cityString = "";}
        String districtString = regionFeignClient.getName(hospital.getDistrictCode());
        hospital.getParam().put("hostypeString", hostypeString);
        hospital.getParam().put("fullAddress", provinceString + cityString + districtString + hospital.getAddress());
        return hospital;
    }
}
