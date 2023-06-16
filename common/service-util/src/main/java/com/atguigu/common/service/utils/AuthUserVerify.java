package com.atguigu.common.service.utils;

import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhaoxu
 * @date 2023年06月14日 19:28
 */
@Component
@Slf4j
public class AuthUserVerify {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 校验是否登录，返回用户id
     *
     * @param request
     * @return java.lang.Long
     * @author liuzhaoxu
     * @date 2023/6/14 19:39
     */

    public Long checkAuth(HttpServletRequest request,
                          HttpServletResponse response) {
        log.info("权限校验开始");
        String token = CookieUtils.getCookie(request, "token");
        log.info("获取到token：" + token);
        if (StringUtils.isEmpty(token)) {
            return refreshToken(request, response);
        }
        Object userVoObj = redisTemplate.opsForValue().get("user:token:" + token);
        if (StringUtils.isEmpty(userVoObj)) {
            return refreshToken(request, response);
        }

        UserVo userVo = (UserVo) userVoObj;
        return userVo.getUserId();
    }

    /**
     * 保存权限
     *
     * @param response
     * @param userVo
     * @author liuzhaoxu
     * @date 2023/6/16 19:02
     */

    public void saveAuth(HttpServletResponse response, UserVo userVo) {
//        token过期时间
        int expirationTime = 60 * 30;
        String token = getToken();
        String refreshToken = getToken();
        log.info("生成令牌=" + token);
        log.info("生成延时令牌=" + refreshToken);

        redisTemplate.opsForValue().set("user:token:" + token, userVo, expirationTime, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("user:refreshToken:" + refreshToken, userVo, expirationTime * 2, TimeUnit.SECONDS);


        CookieUtils.setCookie(response, "token", token, expirationTime);
        CookieUtils.setCookie(response, "refreshToken", refreshToken, expirationTime * 2);
        CookieUtils.setCookie(response, "name", URLEncoder.encode(userVo.getName()), expirationTime * 2);
        CookieUtils.setCookie(response, "headimgurl", URLEncoder.encode(userVo.getHeadimgurl()), expirationTime * 2);
    }

    /**
     * 刷新token
     *
     * @param request
     * @param response
     * @author liuzhaoxu
     * @date 2023/6/16 19:14
     */

    public Long refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookie(request, "refreshToken");
        if (refreshToken == null) {
            log.info("refreshToken为空");
            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
        }
        Object userVoObj = redisTemplate.opsForValue().get("user:refreshToken:" + refreshToken);
        if (StringUtils.isEmpty(userVoObj)) {
            log.info("redis中refreshToken为空");
            throw new GuiguException(ResultCodeEnum.LOGIN_TIMEOUT);
        }
        this.saveAuth(response, (UserVo) userVoObj);
        return ((UserVo) userVoObj).getUserId();
    }

    /**
     * 获取token
     *
     * @return java.lang.String
     * @author liuzhaoxu
     * @date 2023/6/16 19:02
     */

    private String getToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
