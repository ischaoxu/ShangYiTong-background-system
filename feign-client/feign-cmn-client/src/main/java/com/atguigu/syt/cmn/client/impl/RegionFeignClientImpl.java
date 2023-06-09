package com.atguigu.syt.cmn.client.impl;

import com.atguigu.syt.cmn.client.RegionFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaoxu
 * @date 2023年06月09日 20:12
 */
@Component
public class RegionFeignClientImpl implements RegionFeignClient {
    @Override
    public String getName(String code) {
        return "数据获取失败";
    }
}
