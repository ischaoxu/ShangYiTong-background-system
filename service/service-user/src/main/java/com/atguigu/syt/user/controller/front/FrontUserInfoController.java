package com.atguigu.syt.user.controller.front;

import com.atguigu.common.service.utils.AuthUserVerify;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuzhaoxu
 * @date 2023年06月14日 19:54
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/front/user/userInfo")
public class FrontUserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AuthUserVerify authUserVerify;

    @ApiOperation(value = "用户认证")
    @ApiImplicitParam(name = "userAuthVo", value = "用户实名认证对象", required = true)
    @PostMapping("/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        Long userId = authUserVerify.checkAuth(request,response);
        userInfoService.updateUserInfo(userAuthVo, userId);
        return Result.ok();
    }
    @ApiOperation(value = "获取认证信息")
    @GetMapping("/auth/getUserInfo")
    public Result<UserInfo> getUserInfo(HttpServletRequest request,
                                        HttpServletResponse response) {

        Long userId = authUserVerify.checkAuth(request,response);
        UserInfo userInfo = userInfoService.getUserInfoById(userId);
        return Result.ok(userInfo);
    }
}
