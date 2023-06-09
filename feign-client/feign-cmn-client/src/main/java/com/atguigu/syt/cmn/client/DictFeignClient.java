package com.atguigu.syt.cmn.client;

import com.atguigu.syt.cmn.client.impl.DictFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-cmn" ,
        contextId = "dictFeignClient",
        fallback = DictFeignClientImpl.class)
public interface DictFeignClient  {
    /** 
     *  获取数据字典医院等级名称
     * @author liuzhaoxu 
     * @date 2023/6/9 19:10
     * @param dictTypeId
     * @param value 
     * @return java.lang.String 
     */
    @GetMapping(value = "/inner/cmn/dict/getName/{dictTypeId}/{value}")
     String getName(@PathVariable("dictTypeId") Long dictTypeId, @PathVariable("value") String value);
}
