package com.atguigu.syt.hosp.controller.api;

import com.atguigu.common.service.utils.HttpRequestHelper;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.model.hosp.Department;
import com.atguigu.syt.model.hosp.Hospital;
import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月07日 18:38
 */
@Api("医院信息API接口")
@RestController
@RequestMapping("/api/hosp")
public class HospitalController {

    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private ScheduleService scheduleService;
    @ApiOperation("保存医院信息")
    @PostMapping("/saveHospital")
    public Result uploadhospitalData(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//        验证数字签名
//        获取密钥
        HospitalSet hospital = hospitalSetService.getHospitalByHoscode((String) paramMap.get("hoscode"));
//        计算摘要
//        String sign = HttpRequestHelper.getSign(paramMap, hospital.getSignKey());
//        对比摘要
        HttpRequestHelper.checkSign(paramMap,  hospital.getSignKey());
        hospitalService.save(paramMap,hospital.getHoscode());
        return Result.ok();
    }

    @ApiOperation("上传科室")
    @PostMapping("/saveDepartment")
    public Result uploadDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//        验证数字签名
//        获取密钥
        HospitalSet hospital = hospitalSetService.getHospitalByHoscode((String) paramMap.get("hoscode"));
//        计算摘要
//        String sign = HttpRequestHelper.getSign(paramMap, hospital.getSignKey());
//        对比摘要
        HttpRequestHelper.checkSign(paramMap,  hospital.getSignKey());
        departmentService.save(paramMap,hospital.getHoscode(),(String)paramMap.get("depcode"));
        return Result.ok();
    }

    @ApiOperation("上传排班")
    @PostMapping("/saveSchedule")
    public Result uploadSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//        验证数字签名
//        获取密钥
        HospitalSet hospital = hospitalSetService.getHospitalByHoscode((String) paramMap.get("hoscode"));
//        计算摘要
//        String sign = HttpRequestHelper.getSign(paramMap, hospital.getSignKey());
//        对比摘要
        HttpRequestHelper.checkSign(paramMap,  hospital.getSignKey());
        scheduleService.save(paramMap,hospital.getHoscode(),(String)paramMap.get("hosScheduleId"));
        return Result.ok();
    }

    @ApiOperation("查询医院")
    @PostMapping("/hospital/show")
    public Result showHospital(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//        验证数字签名
//        获取密钥
        String hoscode = (String) paramMap.get("hoscode");
        HospitalSet hospital = hospitalSetService.getHospitalByHoscode(hoscode);
//        计算摘要
//        String sign = HttpRequestHelper.getSign(paramMap, hospital.getSignKey());
//        对比摘要
        HttpRequestHelper.checkSign(paramMap,  hospital.getSignKey());
        Hospital hospitalData =  hospitalService.findHospital(hoscode);
        return Result.ok(hospitalData);
    }

    @ApiOperation("获取科室列表")
    @PostMapping("/department/list")
    public Result listDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//        验证数字签名
//        获取密钥
        String hoscode = (String) paramMap.get("hoscode");
        HospitalSet hospital = hospitalSetService.getHospitalByHoscode(hoscode);
//        计算摘要
//        String sign = HttpRequestHelper.getSign(paramMap, hospital.getSignKey());
//        对比摘要
        HttpRequestHelper.checkSign(paramMap,  hospital.getSignKey());
//        获取分页信息
//        page
//        limit
        Integer page = Integer.parseInt((String) paramMap.get("page"));
        Integer limit = Integer.parseInt((String) paramMap.get("limit"));
        Page<Department> departmentList = departmentService.getDepartmentList(page,limit,hoscode);
        return Result.ok(departmentList);
    }

    @ApiOperation("查询排班")
    @PostMapping("/schedule/list")
    public Result showSchedulePage(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//        验证数字签名
//        获取密钥
        String hoscode = (String) paramMap.get("hoscode");
        HospitalSet hospital = hospitalSetService.getHospitalByHoscode(hoscode);
//        计算摘要
//        String sign = HttpRequestHelper.getSign(paramMap, hospital.getSignKey());
//        对比摘要
        HttpRequestHelper.checkSign(paramMap,  hospital.getSignKey());
//        获取分页信息
//        page
//        limit
        Integer page = Integer.parseInt((String) paramMap.get("page"));
        Integer limit = Integer.parseInt((String) paramMap.get("limit"));
        Page<Schedule> schedulePage = scheduleService.listSchedulePage(page,limit,hoscode);
        return Result.ok(schedulePage);
    }



}
