package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
public interface HospitalSetService extends IService<HospitalSet> {

    /**
     *  分页条件查询
     * @author liuzhaoxu
     * @date 2023/5/31 19:48
     * @param page
     * @param limit
     * @param hospitalSetQueryVo
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.atguigu.syt.model.hosp.HospitalSet>
     */

    Page<HospitalSet> selectPage(Long page, Long limit, HospitalSetQueryVo hospitalSetQueryVo);
    /**
     *  根据分配给医院的唯一标识获取医院信息
     * @author liuzhaoxu
     * @date 2023/6/7 18:54
     * @param hoscode
     * @return com.atguigu.syt.model.hosp.Hospital
     */

    HospitalSet getHospitalByHoscode(String hoscode);

}
