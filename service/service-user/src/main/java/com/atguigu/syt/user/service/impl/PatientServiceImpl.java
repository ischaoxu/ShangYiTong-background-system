package com.atguigu.syt.user.service.impl;

import com.atguigu.syt.cmn.client.DictFeignClient;
import com.atguigu.syt.cmn.client.RegionFeignClient;
import com.atguigu.syt.enums.DictTypeEnum;
import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.mapper.PatientMapper;
import com.atguigu.syt.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-12
 */
@Service
 public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;
    @Autowired
    private RegionFeignClient regionFeignClient;


    @Override
    public Patient getPatient(Long id, Long userId) {

        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId);
        wrapper.eq(Patient::getId, id);
        Patient patient = baseMapper.selectOne(wrapper);
        //封装数据
        return this.packPatient(patient);
    }

    @Override
    public List<Patient> findByUserId(Long userId) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getUserId, userId);

        List<Patient> patientList = baseMapper.selectList(queryWrapper);
        patientList.forEach(patient -> {
            patient.getParam().put("expenseMethod", patient.getIsInsure()==0?"自费":"医保");
        });
        return patientList;
    }

    private Patient packPatient(Patient patient) {
        String certificatesTypeString = dictFeignClient.getName(DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId(), patient.getCertificatesType());
        String contactsCertificatesTypeString = dictFeignClient.getName(
                DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId(), patient.getContactsCertificatesType());
        String provinceString = regionFeignClient.getName(patient.getProvinceCode());
        String cityString = regionFeignClient.getName(patient.getCityCode());
        String districtString = regionFeignClient.getName(patient.getDistrictCode());

        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress",
                provinceString + cityString + districtString + patient.getAddress());
        return patient;

    }
}
