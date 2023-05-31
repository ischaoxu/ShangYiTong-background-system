package com.atguigu.common.system.service.impl;

import com.atguigu.common.system.model.SysDept;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.system.helper.DeptHelper;
import com.atguigu.common.system.mapper.SysDeptMapper;
import com.atguigu.common.system.service.SysDeptService;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;


    @Override
    public List<SysDept> findNodes() {
        List<SysDept> goodsTypeList = this.list();
        return DeptHelper.buildTree(goodsTypeList, 0L);
    }

    @Override
    public List<SysDept> findUserNodes() {
        List<SysDept> sysDeptList = this.list(new LambdaQueryWrapper<SysDept>().eq(SysDept::getStatus, 1));
        return DeptHelper.buildTree(sysDeptList, 0L);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysDept sysDept = this.getById(id);
        sysDept.setStatus(status);
        this.updateById(sysDept);
    }

    @Override
    public boolean removeById(Serializable id) {
        int count = this.count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        if(count > 0) {
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }
        sysDeptMapper.deleteById(id);
        return false;
    }

}

