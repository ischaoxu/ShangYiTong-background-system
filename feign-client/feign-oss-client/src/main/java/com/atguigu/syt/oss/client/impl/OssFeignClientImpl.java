package com.atguigu.syt.oss.client.impl;

import com.atguigu.syt.oss.client.OssFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaoxu
 * @date 2023年06月14日 21:01
 */
@Component
public class OssFeignClientImpl implements OssFeignClient {
    @Override
    public String getPreviewUrl(String objectName) {
        return "图片获取失败";
    }
}
