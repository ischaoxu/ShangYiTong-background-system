package com.atguigu.syt.user.service.impl;

import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.mapper.PatientMapper;
import com.atguigu.syt.user.service.PatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
