package com.atguigu.syt.user.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuzhaoxu
 * @date 2023年06月12日 20:40
 */

@Configuration
@ConfigurationProperties(prefix="wx.open") //读取节点
@Data //使用set方法将wx.ope节点中的值填充到当前类的属性中
public class ConstantProperties {
    private String appId;
    private String appSecret;
    private String redirectUri;
    private String sytBaseUrl;
}