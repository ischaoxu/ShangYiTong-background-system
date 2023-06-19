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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author liuzhaoxu
 * @date 2023年06月07日 19:19
 */
@Service
@Slf4j
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
        return this.packHospital(hospitalRepository.getByHoscode(hoscode));
    }

    @Override
    public List<Hospital> listHospitalSearch(String hosname, String hostype, String districtCode) {
//        排序条件
        Sort sort = Sort.by(Sort.Direction.ASC, "hoscode");
//        查询模板
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("hosname", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("hostype", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("districtCode", ExampleMatcher.GenericPropertyMatchers.exact());
//                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact());
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

        Long a = System.currentTimeMillis();
        log.info("开始记录：" + a);
        hospitalList.forEach(this::packHospital);
        Long b = System.currentTimeMillis();
        log.info("结束记录：" + b);
        log.info("字典翻译用时：" + (b - a) + "ms");
        return hospitalList;
    }



    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;

    private Hospital packHospital(Hospital hospital) {
        String hostypeString = dictFeignClient.getName(DictTypeEnum.HOSTYPE.getDictTypeId(), hospital.getHostype());
        hospital.getParam().put("hostypeString", hostypeString);


        ArrayList<String> params = new ArrayList<>(3);
        params.add(hospital.getProvinceCode());
        params.add(hospital.getCityCode());
        params.add(hospital.getDistrictCode());
        List<String> nameList = params.stream().map(s -> CompletableFuture.supplyAsync(() -> {
            String name = regionFeignClient.getName(s);
            return name;
        }, executor).exceptionally(e->{
            log.warn("字典翻译出错："+e.getLocalizedMessage());
            return null;
        })).collect(Collectors.toList()).stream().map(name -> name.join()).collect(Collectors.toList());
        StringBuffer baseAddress = new StringBuffer();
        if (nameList.get(0).equals(nameList.get(1))) {
            nameList.remove(1);
        }
        nameList.forEach(s->{
            baseAddress.append(s);
        });


//        String provinceString = regionFeignClient.getName(hospital.getProvinceCode());
//        String cityString = regionFeignClient.getName(hospital.getCityCode());
//        if(provinceString.equals(cityString)) {cityString = "";}
//        String districtString = regionFeignClient.getName(hospital.getDistrictCode());
//        hospital.getParam().put("fullAddress", provinceString + cityString + districtString + hospital.getAddress());


        hospital.getParam().put("fullAddress", baseAddress+ hospital.getAddress());

        return hospital;
    }
}
