package com.atguigu.common.system.service.impl;


import com.atguigu.common.system.model.SysUser;
import com.atguigu.common.system.mapper.SysUserMapper;
import com.atguigu.common.system.service.SysMenuService;
import com.atguigu.common.system.service.SysUserService;
import com.atguigu.common.system.vo.RouterVo;
import com.atguigu.common.system.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo userQueryVo){

        return sysUserMapper.selectPage(pageParam, userQueryVo);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        //创建SysUser对象
        SysUser sysUser = new SysUser();
        //设置id
        sysUser.setId(id);
        //设置用户状态
        sysUser.setStatus(status);
        //调用SysUserMapper中更新的方法
        sysUserMapper.updateById(sysUser);
    }

    @Override
    public SysUser getByUsername(String username) {

        return sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        SysUser sysUser = this.getByUsername(username);

        //根据用户id获取菜单权限值
        List<RouterVo> routerVoList = sysMenuService.findUserMenuList(sysUser.getId());
        //根据用户id获取用户按钮权限
        List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());

        result.put("name", sysUser.getName());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //当前权限控制使用不到，我们暂时忽略
        result.put("roles",  new HashSet<>());
        result.put("buttons", permsList);
        result.put("routers", routerVoList);
        return result;
    }

}