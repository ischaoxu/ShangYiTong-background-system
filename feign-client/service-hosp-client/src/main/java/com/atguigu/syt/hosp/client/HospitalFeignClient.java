package com.atguigu.syt.hosp.client;

import com.atguigu.syt.hosp.client.impl.HospitalFeignClientImpl;
import com.atguigu.syt.vo.order.SignInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 20:37
 */
@FeignClient(
        value = "service-hosp",
        contextId = "hospitalFeignClient",
        fallback = HospitalFeignClientImpl.class
)
public interface HospitalFeignClient {

    @GetMapping("/inner/hosp/hospital/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(@PathVariable("hoscode") String hoscode);

}
