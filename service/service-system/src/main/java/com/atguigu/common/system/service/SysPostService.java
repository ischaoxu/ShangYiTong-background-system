package com.atguigu.common.system.service;

import com.atguigu.common.system.model.SysPost;
import com.atguigu.common.system.vo.SysPostQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPostService extends IService<SysPost> {

    IPage<SysPost> selectPage(Page<SysPost> pageParam, SysPostQueryVo sysPostQueryVo);

    void updateStatus(Long id, Integer status);

    List<SysPost> findAll();
}

