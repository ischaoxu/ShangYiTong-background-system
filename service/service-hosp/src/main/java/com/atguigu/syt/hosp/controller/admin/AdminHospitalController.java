package com.atguigu.syt.hosp.controller.admin;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhaoxu
 * @date 2023年06月09日 18:24
 */
@Api(tags = "医院管理")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class AdminHospitalController {


    @Autowired
    private HospitalService hospitalService;
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码", required = true),
            @ApiImplicitParam(name = "limit",value = "每页记录数", required = true),
            @ApiImplicitParam(name = "hosname",value = "查询字符串")})
    @GetMapping("/{page}/{limit}")
    public Result<Page<Hospital>> showHostpitalPage(@PathVariable("page") Integer page,
                                                    @PathVariable("limit") Integer limit,
                                                    String hosname) {

        Page<Hospital> hospitalPage = hospitalService.pageHospital(page, limit, hosname);
        return Result.ok(hospitalPage);

    }

    @ApiOperation(value = "更新上线状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hoscode",value = "医院编码", required = true),
            @ApiImplicitParam(name = "status",value = "状态（0：未上线 1：已上线）", required = true)})
    @GetMapping("/updateStatus/{hoscode}/{status}")
    public Result updateStatus(@PathVariable("hoscode") String hoscode, @PathVariable("status") Integer status){
        hospitalService.updateStatus(hoscode, status);
        return Result.ok();
    }


}
