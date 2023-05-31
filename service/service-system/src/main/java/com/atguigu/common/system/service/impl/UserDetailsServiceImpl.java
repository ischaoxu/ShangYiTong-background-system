package com.atguigu.common.system.service.impl;

import com.atguigu.common.security.custom.CustomUser;
import com.atguigu.common.system.model.SysUser;
import com.atguigu.common.system.service.SysMenuService;
import com.atguigu.common.system.service.SysUserService;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getByUsername(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new DisabledException("账号已停用");
        }

        List<String> userPermsList = sysMenuService.findUserPermsList(sysUser.getId());
        /*List<SimpleGrantedAuthority> authorities = userPermsList.stream()
                .filter(code -> !StringUtils.isEmpty(code.trim()))
                .map(code -> new SimpleGrantedAuthority(code.trim()))
                .collect(Collectors.toList());
        return new CustomUser(sysUser, authorities);*/
        sysUser.setUserPermsList(userPermsList);
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
