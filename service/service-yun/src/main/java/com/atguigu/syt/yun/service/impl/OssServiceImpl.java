package com.atguigu.syt.yun.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.yun.service.OssService;
import com.atguigu.syt.yun.util.OssAliyunProperties;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liuzhaoxu
 * @date 2023年06月14日 12:58
 */
@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private OssAliyunProperties ossAliyunProperties;

    @Override
    public Map<String, String> uploadFile(MultipartFile file) {
//        权限校验





        if (file.getSize()<1) {
            throw new GuiguException(ResultCodeEnum.FAIL);
        }
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(ossAliyunProperties.getEndpoint(),
                    ossAliyunProperties.getKeyId(),
                    ossAliyunProperties.getKeySecret());
//        准备文件名称
            String baseFileName = UUID.randomUUID().toString().replaceAll("-", "")
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        准备文件夹名称
            String folder = new LocalDate().toString();
            // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
            String objectName = folder +"/"+ baseFileName;

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossAliyunProperties.getBucketName(),
                    objectName,
                    file.getInputStream());
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
//            获取文件临时访问地址
            if (result.getResponse().getStatusCode() != 200) {
                throw new GuiguException(ResultCodeEnum.FAIL);
            }
            // 设置签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000L);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(ossAliyunProperties.getBucketName(), objectName, expiration);

            HashMap<String, String> map = new HashMap<>(2);

            map.put("previewUrl", url.toString()); //页面中授权预览图片
            map.put("url", objectName); //数据库存储

            return map;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());

            throw new GuiguException(ResultCodeEnum.FAIL, oe);

        } catch (GuiguException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());

            throw new GuiguException(ResultCodeEnum.FAIL, ce);

        } catch (IOException e) {

            throw new GuiguException(ResultCodeEnum.FAIL, e);

        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

    }

    @Override
    public String getPreviewUrl(String objectName) {
        String endpoint = ossAliyunProperties.getEndpoint();
        String accessKeyId = ossAliyunProperties.getKeyId();
        String accessKeySecret = ossAliyunProperties.getKeySecret();
        String bucketName = ossAliyunProperties.getBucketName();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置URL过期时间为1小时，单位：毫秒
        Date expiration = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        return url.toString();
    }
}
