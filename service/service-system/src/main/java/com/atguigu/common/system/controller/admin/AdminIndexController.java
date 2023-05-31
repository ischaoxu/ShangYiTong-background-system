package com.atguigu.common.system.controller.admin;

import com.atguigu.common.system.model.SysUser;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.system.service.SysUserService;
import com.atguigu.common.util.result.Result;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.common.util.tools.MD5;
import com.atguigu.common.system.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class AdminIndexController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 登录
     * @return
     */
    /*@PostMapping("/login")
    public Result login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token","admin");
        return Result.ok(map);
    }*/
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        SysUser sysUser = sysUserService.getByUsername(loginVo.getUsername());
        if(null == sysUser) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        if(!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())) {
            throw new GuiguException(ResultCodeEnum.PASSWORD_ERROR);
        }
        if(sysUser.getStatus().intValue() == 0) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }

        Map<String, Object> map = new HashMap<>();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.boundValueOps(token).set(sysUser,2, TimeUnit.HOURS);
        map.put("token",token);
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     * @return
     */
    /*@GetMapping("/info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }*/
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        //获取请求头中的token
        String token = request.getHeader("token");
        //根据token获取Redis中的用户信息
        SysUser sysUser = (SysUser)redisTemplate.boundValueOps("adminsys:token:" + token).get();
        Map<String, Object> map = sysUserService.getUserInfo(sysUser.getUsername());
        return Result.ok(map);
    }

    /**
     * 退出
     * @return
     */
    /*@PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }*/
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        //获取请求头中的token
        String token = request.getHeader("token");
        //删除Redis中的用户信息
        redisTemplate.delete("adminsys:token:" + token);
        return Result.ok();
    }
}
