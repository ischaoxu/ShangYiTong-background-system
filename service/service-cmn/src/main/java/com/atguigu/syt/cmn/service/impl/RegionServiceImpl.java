package com.atguigu.syt.cmn.service.impl;


import com.alibaba.excel.EasyExcel;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.cmn.easyexcel.RegionExcelVoListener;
import com.atguigu.syt.cmn.mapper.RegionMapper;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
@Slf4j
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

    @Override
    public void saveRegionExcelVoBatch(List<RegionExcelVo> cachedDataList) {
        baseMapper.insertRegionExcelVoList(cachedDataList);
    }

    @Override
    public void importData(MultipartFile file) {
        try {
            long a = System.currentTimeMillis();
            EasyExcel.read(file.getInputStream(), RegionExcelVo.class, new RegionExcelVoListener(this)).sheet().doRead();
            long b = System.currentTimeMillis();
            log.info("耗时:"+(b-a)+"ms");
        } catch (IOException e) {
            throw new GuiguException(ResultCodeEnum.FAIL, e);
        }
    }

    @Override
    public String getNameByCode(String code) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Region::getCode, code);
        Region region = baseMapper.selectOne(wrapper);
        if (region == null) {
            return "";
        }
        return region.getName();
    }

}
