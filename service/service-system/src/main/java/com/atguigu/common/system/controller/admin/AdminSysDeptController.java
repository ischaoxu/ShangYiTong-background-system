package com.atguigu.common.system.controller.admin;


import com.atguigu.common.system.model.SysDept;
import com.atguigu.common.system.service.SysDeptService;
import com.atguigu.common.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "部门管理")
@RestController
@RequestMapping("/admin/system/sysDept")
public class AdminSysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysDept sysDept = sysDeptService.getById(id);
        return Result.ok(sysDept);
    }

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public Result save(@RequestBody SysDept sysDept) {
        sysDeptService.save(sysDept);
        return Result.ok();
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysDept sysDept) {
        sysDeptService.updateById(sysDept);
        return Result.ok();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysDeptService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "获取全部部门节点")
    @GetMapping("/findNodes")
    public Result findNodes() {
        return Result.ok(sysDeptService.findNodes());
    }

    @ApiOperation(value = "获取用户部门节点")
    @GetMapping("/findUserNodes")
    public Result findUserNodes() {
        return Result.ok(sysDeptService.findUserNodes());
    }

    @ApiOperation(value = "更新状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysDeptService.updateStatus(id, status);
        return Result.ok();
    }

}
