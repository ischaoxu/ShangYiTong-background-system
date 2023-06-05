package com.atguigu.syt.cmn.service.impl;


import com.atguigu.syt.cmn.mapper.RegionMapper;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-04
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    @Override
    @Cacheable(value = "regionList", key = "#parentCode", unless="#result.size() == 0")
    public List<Region> findRegionListByParentCode(String parentCode) {
        List<Region> regionList = baseMapper.selectList(new LambdaQueryWrapper<Region>().eq(Region::getParentCode, parentCode));
        regionList.forEach(region -> {
            boolean isHasChildren = region.getLevel().intValue() < 3 ? true : false;
            region.setHasChildren(isHasChildren);
        });

        return regionList;
    }

    @Override
    @Cacheable(value = "regionexcelvolist", unless="#result.size() == 0")
    public List<RegionExcelVo> findRegionExcelVoList() {
        List<RegionExcelVo> regionexcelvolist = new ArrayList<>();
        List<Region> regionList = baseMapper.selectList(null);
        regionList.forEach(region -> {
            RegionExcelVo regionExcelVo = new RegionExcelVo();
            BeanUtils.copyProperties(region,regionExcelVo);
            regionexcelvolist.add(regionExcelVo);
        });
        return regionexcelvolist;
    }
}
