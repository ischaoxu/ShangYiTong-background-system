package com.atguigu.syt.yun.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface OssService {
    /**
     *  上传文件
     * @author liuzhaoxu
     * @date
     * 18:28
     * @param file
     * @return java.util.Map<java.lang.String,java.lang.String>
     */

    Map<String, String> uploadFile(MultipartFile file);
    /**
     *  获取图片url地址
     * @author liuzhaoxu
     * @date 2023/6/14 20:58
     * @param objectName
     * @return java.lang.String
     */

    String getPreviewUrl(String objectName);
}
