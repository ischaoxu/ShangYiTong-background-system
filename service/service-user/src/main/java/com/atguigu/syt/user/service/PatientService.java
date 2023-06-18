package com.atguigu.syt.user.service;

import com.atguigu.syt.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-12
 */
public interface PatientService extends IService<Patient> {

    /**
     *  根据id和userid获取就诊人信息
     * @author liuzhaoxu
     * @date 2023/6/16 20:13
     * @param id
     * @param userId
     * @return com.atguigu.syt.model.user.Patient
     */

    Patient getPatient(Long id, Long userId);

    /**
     *  根据userid获取名下所有就诊人
     * @author liuzhaoxu
     * @date 2023/6/16 20:22
     * @param userId
     * @return java.util.List<com.atguigu.syt.model.user.Patient>
     */

    List<Patient> findByUserId(Long userId);

    /**
     *  根据id，userid删除Patient
     * @author liuzhaoxu
     * @date 2023/6/17 8:22
     * @param id
     * @param userId
     * @return com.atguigu.syt.model.user.Patient
     */

     void  removePatient(Long id, Long userId);

    /**
     *  根据根据用户id获取就诊人列表
     * @author liuzhaoxu
     * @date 2023/6/18 23:25
     * @param userId
     * @return java.util.List<com.atguigu.syt.model.user.Patient>
     */

    List<Patient> ListPatientByUserId(Long userId);

    Patient packPatient(Patient patient);

}
