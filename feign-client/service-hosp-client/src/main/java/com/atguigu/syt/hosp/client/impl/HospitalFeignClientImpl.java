package com.atguigu.syt.hosp.client.impl;

import com.atguigu.syt.hosp.client.HospitalFeignClient;
import com.atguigu.syt.vo.order.SignInfoVo;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 20:38
 */
@Component
public class HospitalFeignClientImpl implements HospitalFeignClient {
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        return null;
    }
}
