package com.atguigu.syt.hosp.service.impl;

import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.hosp.mapper.HospitalSetMapper;
import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    static Logger logger= LoggerFactory.getLogger("医院设置管理日志");
    @Override
    public Page<HospitalSet> selectPage(Long page, Long limit, HospitalSetQueryVo hospitalSetQueryVo) {
        //1封装分页参数
        Page<HospitalSet> pageParam = new Page<>(page,limit);
        logger.info("封装分页,页数:"+page+"步长:"+limit);
        //2从hospitalSetQueryVo取出参数
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        //3验空拼写查询条件
        LambdaQueryWrapper<HospitalSet> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(hosname),HospitalSet::getHosname,hosname);
        wrapper.eq(!StringUtils.isEmpty(hoscode),HospitalSet::getHoscode,hoscode);
        //4进行带条件分页查询
        baseMapper.selectPage(pageParam, wrapper);
        logger.info("进行了带条件分页查询");
        return pageParam;
    }

    @Override
    public HospitalSet getHospitalByHoscode(String hoscode) {
        HospitalSet hospitalSet = baseMapper.selectOne(new LambdaQueryWrapper<HospitalSet>().eq(HospitalSet::getHoscode, hoscode));
        if (hospitalSet != null) {
            return hospitalSet;
        }else if (hospitalSet.getStatus() == 0) {
            throw new GuiguException(ResultCodeEnum.HOSPITAL_LOCK);
        }else {
                throw new GuiguException(ResultCodeEnum.HOSPITAL_OPEN);
            }
        }

}
