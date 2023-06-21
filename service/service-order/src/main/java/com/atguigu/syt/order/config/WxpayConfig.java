package com.atguigu.syt.order.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuzhaoxu
 * @date 2023年06月20日 11:51
 */
@Configuration
public class WxpayConfig {
    @Autowired
    private WxpayProperties wxpayProperties;
    @Bean
    public RSAAutoCertificateConfig getRSAAutoCertificateConfig() {
        RSAAutoCertificateConfig build = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxpayProperties.getMchId())
                .privateKeyFromPath(wxpayProperties.getPrivateKeyPath())
                .merchantSerialNumber(wxpayProperties.getMchSerialNo())
                .apiV3Key(wxpayProperties.getApiV3Key())
                .build();
        return build;
    }


}
