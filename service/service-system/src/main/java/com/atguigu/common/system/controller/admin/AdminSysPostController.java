package com.atguigu.common.system.controller.admin;


import com.atguigu.common.system.model.SysPost;
import com.atguigu.common.system.service.SysPostService;
import com.atguigu.common.util.result.Result;
import com.atguigu.common.system.vo.SysPostQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "岗位管理")
@RestController
@RequestMapping("/admin/system/sysPost")
public class AdminSysPostController {

    @Resource
    private SysPostService sysPostService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "sysPostVo", value = "查询对象", required = false)
            SysPostQueryVo sysPostQueryVo) {
        Page<SysPost> pageParam = new Page<>(page, limit);
        IPage<SysPost> pageModel = sysPostService.selectPage(pageParam, sysPostQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysPost sysPost = sysPostService.getById(id);
        return Result.ok(sysPost);
    }

    @GetMapping("/findAll")
    public Result findAll() {
        return Result.ok(sysPostService.findAll());
    }

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public Result save(@RequestBody SysPost sysPost) {
        sysPostService.save(sysPost);
        return Result.ok();
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysPost sysPost) {
        sysPostService.updateById(sysPost);
        return Result.ok();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysPostService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "更新状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysPostService.updateStatus(id, status);
        return Result.ok();
    }

}
