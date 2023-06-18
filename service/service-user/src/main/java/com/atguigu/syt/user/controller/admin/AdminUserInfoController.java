package com.atguigu.syt.user.controller.admin;


import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-06-12
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/user/userInfo")
public class AdminUserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @ApiOperation("分页条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true),
            @ApiImplicitParam(name = "limit", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "userInfoQueryVo", value = "查询对象", required = false)
    })
    @GetMapping("/{page}/{limit}")
    public Result<IPage<UserInfo>> showUserPage(@PathVariable("page") Integer page,
                                               @PathVariable("limit") Integer limit,
                                               UserInfoQueryVo userInfoQueryVo) {
        IPage<UserInfo> userPage = userInfoService.lisetUserPage(page,limit,userInfoQueryVo);
        return Result.ok(userPage);
    }
    @ApiOperation("锁定和解锁")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(name = "status",value = "用户状态",required = true)
    })
    @PutMapping("lock/{userId}/{status}")
    public Result lock(
            @PathVariable("userId") Long userId,
            @PathVariable("status") Integer status){
        boolean result = userInfoService.lock(userId, status);
        if(result){
            return Result.ok().message("设置成功");
        }else{
            return Result.ok().message("设置失败");
        }
    }@ApiOperation("用户详情")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true)
    @GetMapping("show/{userId}")
    public Result<Map<String, Object>> show(@PathVariable Long userId) {
        return Result.ok(userInfoService.show(userId));
    }
    @ApiOperation("认证审批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(name = "authStatus",value = "用户认证审批状态",required = true)
    })
    @PutMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId, @PathVariable Integer authStatus) {
        boolean result = userInfoService.approval(userId,authStatus);
        if(result){
            return Result.ok().message("审批成功");
        }else{
            return Result.ok().message("审批失败");
        }
    }


}

