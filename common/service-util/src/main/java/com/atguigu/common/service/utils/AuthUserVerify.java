package com.atguigu.common.service.utils;

import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

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
     *  校验是否登录，返回用户id
     * @author liuzhaoxu
     * @date 2023/6/14 19:39
     * @param request
     * @return java.lang.Long
     */

    public Long checkAuth(HttpServletRequest request) {
        log.info("权限校验开始");
        String token = request.getHeader("token");
        log.info("获取到token："+token);
        if(StringUtils.isEmpty(token)) {
            log.info("token为空");
            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
        }
        Object userObj = redisTemplate.opsForValue().get("user:token:" + token);
        if (StringUtils.isEmpty(userObj)) {
            log.info("redis中token为空");
            throw new GuiguException(ResultCodeEnum.LOGIN_TIMEOUT);
        }
        Long userId = null;
        if (userObj instanceof Integer) {
            userId = ((Integer) userObj).longValue();
        } else if (userObj instanceof Long) {
            userId = ((Long) userObj).longValue();
        } else if (userObj instanceof String) {
            userId = Long.parseLong((String) userObj);
        }
        log.info("校验通过，得到userId："+userId);
        return userId;
    }

}
