package com.atguigu.common.system.mapper;

import com.atguigu.common.system.model.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findListByUserId(@Param("userId") Long userId);

    List<SysMenu> selectMenuListByUserId(Long userId);
}
