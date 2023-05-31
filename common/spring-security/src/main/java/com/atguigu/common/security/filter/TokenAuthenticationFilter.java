package com.atguigu.common.security.filter;

import com.atguigu.common.system.model.SysUser;
import com.atguigu.common.util.result.Result;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.common.util.tools.LoginUserInfoUtil;
import com.atguigu.common.util.tools.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证解析token过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        logger.info("uri:"+request.getRequestURI());

        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //放行
            chain.doFilter(request, response);
        } else {
            logger.error("没有权限");
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        Object object = redisTemplate.boundValueOps("adminsys:token:" + token).get();
        if (null == object) {
            return null;
        }

        SysUser sysUser = (SysUser)object;
        LoginUserInfoUtil.setUserId(sysUser.getId());
        LoginUserInfoUtil.setUsername(sysUser.getUsername());

        if (null != sysUser.getUserPermsList() && sysUser.getUserPermsList().size() > 0) {
            //获取用户权限
            List<String> userPermsList = sysUser.getUserPermsList();
            List<SimpleGrantedAuthority> authorities = userPermsList.stream()
                    .filter(code -> !StringUtils.isEmpty(code.trim()))
                    .map(code -> new SimpleGrantedAuthority(code.trim()))
                    .collect(Collectors.toList());
            //返回一个认证之后包含权限的对象
            return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), null, authorities);
        } else {
            //返回一个认证之后没有权限的对象
            return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), null, Collections.emptyList());
        }

    }
}