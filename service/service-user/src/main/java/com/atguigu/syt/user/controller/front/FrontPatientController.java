package com.atguigu.syt.user.controller.front;

import com.atguigu.common.service.utils.AuthUserVerify;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liuzhaoxu
 * @date 2023年06月16日 20:08
 */

@Api(tags = "就诊人管理")
@RestController
@RequestMapping("/front/user/patient")
public class FrontPatientController {

    @Resource
    private PatientService patientService;

    @Resource
    private AuthUserVerify authUserVerify;

    @ApiOperation("添加就诊人")
    @ApiImplicitParam(name = "patient",value = "就诊人对象", required = true)
    @PostMapping("/auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request, HttpServletResponse response) {
        Long userId = authUserVerify.checkAuth(request, response);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }
    @ApiOperation("修改就诊人")
    @ApiImplicitParam(name = "patient",value = "就诊人对象", required = true)
    @PutMapping("/auth/update")
    public Result updatePatient(@RequestBody Patient patient, HttpServletRequest request, HttpServletResponse response) {
        authUserVerify.checkAuth(request, response);
        patientService.save(patient);
        return Result.ok();
    }
    @ApiOperation("根据id获取就诊人信息")
    @ApiImplicitParam(name = "id",value = "就诊人id", required = true)
    @GetMapping("/auth/get/{id}")
    public Result<Patient> getPatient(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Long userId = authUserVerify.checkAuth(request, response);
        Patient patient =  patientService.getPatient(id, userId);
        return Result.ok(patient);

    }
    @ApiOperation("获取就诊人列表")
    @GetMapping("/auth/findAll")
    public Result<List<Patient>> findAll(HttpServletRequest request, HttpServletResponse response) {

        Long userId = authUserVerify.checkAuth(request, response);
        List<Patient> list = patientService.findByUserId(userId);
        return Result.ok(list);
    }

}
