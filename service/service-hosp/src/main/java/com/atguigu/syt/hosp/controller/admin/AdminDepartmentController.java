package com.atguigu.syt.hosp.controller.admin;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuzhaoxu
 * @date 2023年06月11日 16:18
 */
@Api(tags = "科室管理")
@RestController
@RequestMapping("/admin/hosp/department")
public class AdminDepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询医院所有科室列表")
    @ApiImplicitParam(name = "hoscode",value = "医院编码", required = true)
    @GetMapping("/getDeptList/{hoscode}")
    public Result<List<DepartmentVo>> showDepartmentByHoscode(@PathVariable("hoscode") String hoscode) {
        List<DepartmentVo>  departmentList = departmentService.getDepartmentByHoscode(hoscode);
        return Result.ok(departmentList);
    }







}
