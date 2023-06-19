package com.atguigu.syt.user.controller.inner;

import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 20:09
 */

@Api(tags = "就诊人接口-供其他微服务远程调用")
@RestController
@RequestMapping("/inner/user/patient")
public class InnerUserInfoController {
    @Resource
    private PatientService patientService;

    @ApiOperation("获取就诊人")
    @ApiImplicitParam(name = "id",value = "就诊人id", required = true)
    @GetMapping("/get/{id}")
    public Patient getPatient(@PathVariable("id") Long id) {
        return patientService.getById(id);
    }
}
