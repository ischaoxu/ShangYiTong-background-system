package com.atguigu.common.service.utils;

import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
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
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
        }
        Object userObj = redisTemplate.opsForValue().get("user:token:" + token);
        if (StringUtils.isEmpty(userObj)) {
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
        return userId;
    }

}
