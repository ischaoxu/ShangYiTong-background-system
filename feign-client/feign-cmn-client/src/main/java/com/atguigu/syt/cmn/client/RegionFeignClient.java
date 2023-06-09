package com.atguigu.syt.cmn.client;

import com.atguigu.syt.cmn.client.impl.RegionFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-cmn" ,
        contextId = "regionClient",
        fallback = RegionFeignClientImpl.class)
public interface RegionFeignClient {
    /** 
     *  根据地区编码获取地区名称
     * @author liuzhaoxu 
     * @date 2023/6/9 19:12
     * @param code 
     * @return java.lang.String 
     */
    @GetMapping(value = "/inner/cmn/region/getName/{code}")
    String getName(@PathVariable("code") String code);
}
