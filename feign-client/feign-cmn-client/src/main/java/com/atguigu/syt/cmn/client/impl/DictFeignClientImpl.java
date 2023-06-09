package com.atguigu.syt.cmn.client.impl;

import com.atguigu.syt.cmn.client.DictFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaoxu
 * @date 2023年06月09日 20:12
 */
@Component
public class DictFeignClientImpl implements DictFeignClient {
    @Override
    public String getName(Long dictTypeId, String value) {
        return "数据获取失败";
    }
}
