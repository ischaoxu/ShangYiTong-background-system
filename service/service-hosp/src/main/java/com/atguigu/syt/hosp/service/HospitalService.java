package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.Hospital;
import com.atguigu.syt.vo.order.SignInfoVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    /**
     *  保存医院信息
     * @author liuzhaoxu
     * @date 2023/6/7 19:21
     * @param paramMap
     */

    void save(Map<String, Object> paramMap,String hoscode);

    /**
     *  根据医院唯一标识获取医院信息
     * @author liuzhaoxu
     * @date 2023/6/7 20:44
     * @param hoscode
     * @return com.atguigu.syt.model.hosp.Hospital
     */

    Hospital findHospital(String hoscode);

    /**
     *  获取医院分页列表
     * @author liuzhaoxu
     * @date 2023/6/9 18:30
     * @param page
     * @param limit
     * @param hosname
     * @return org.springframework.data.domain.Page<com.atguigu.syt.model.hosp.Hospital>
     */

    Page<Hospital> pageHospital(Integer page, Integer limit, String hosname);

    /**
     *  修改医院状态
     * @author liuzhaoxu
     * @date 2023/6/9 19:36
     * @param hoscode
     * @param status
     */

    void updateStatus(String hoscode, Integer status);

    /**
     *  获取医院详情信息
     * @author liuzhaoxu
     * @date 2023/6/10 8:38
     * @param hoscode
     * @return com.atguigu.syt.model.hosp.Hospital
     */

    Hospital getHospital(String hoscode);

    /**
     *  获取医院列表(支持条件查询)
     * @author liuzhaoxu
     * @date 2023/6/12 12:49
     * @param hosname
     * @param hostype
     * @param districtCode
     * @return java.util.List<com.atguigu.syt.model.hosp.Hospital>
     */
    List<Hospital> listHospitalSearch(String hosname, String hostype, String districtCode);


}
