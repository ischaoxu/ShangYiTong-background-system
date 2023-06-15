package com.atguigu.syt.oss.client;

import com.atguigu.syt.oss.client.impl.OssFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-yun" ,
        contextId = "ossFeignClient",
        fallback = OssFeignClientImpl.class)
public interface OssFeignClient {

    @GetMapping("/inner/yun/file/getPreviewUrl")
    public String getPreviewUrl(@RequestParam String objectName);
}
