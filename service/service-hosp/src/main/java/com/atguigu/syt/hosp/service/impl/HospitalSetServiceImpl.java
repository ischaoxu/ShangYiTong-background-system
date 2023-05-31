package com.atguigu.syt.hosp.service.impl;

import com.atguigu.syt.hosp.mapper.HospitalSetMapper;
import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 医院设置表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public Page<HospitalSet> selectPage(Long page, Long limit, HospitalSetQueryVo hospitalSetQueryVo) {
        //1封装分页参数
        Page<HospitalSet> pageParam = new Page<>(page,limit);
        //2从hospitalSetQueryVo取出参数
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        //3验空拼写查询条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(hosname),"hosname",hosname);
        wrapper.eq(!StringUtils.isEmpty(hoscode),"hoscode",hoscode);
        //4进行带条件分页查询
        baseMapper.selectPage(pageParam, wrapper);
        return pageParam;
    }
}
