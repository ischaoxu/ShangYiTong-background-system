package com.atguigu.syt.user.client.impl;

import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.client.UserFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 20:22
 */
@Component
public class UserFeignClientImpl implements UserFeignClient {

    @Override
    public Patient getPatient(Long id) {

        return null;
    }
}
