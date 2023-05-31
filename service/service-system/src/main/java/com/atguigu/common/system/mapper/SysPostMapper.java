package com.atguigu.common.system.mapper;

import com.atguigu.common.system.model.SysPost;
import com.atguigu.common.system.vo.SysPostQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface SysPostMapper extends BaseMapper<SysPost> {

    IPage<SysPost> selectPage(Page<SysPost> page, @Param("vo") SysPostQueryVo sysPostQueryVo);
}
