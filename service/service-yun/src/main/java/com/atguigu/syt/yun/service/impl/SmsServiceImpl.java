package com.atguigu.syt.yun.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.vo.sms.SmsVo;
import com.atguigu.syt.yun.service.SmsService;
import com.atguigu.syt.yun.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月21日 18:02
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Override
    public void send(SmsVo smsVo) {

        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "f7b00eb6876f46878fec0f25bf5728e3";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();

        StringBuffer contentBuffer = new StringBuffer();
        smsVo.getParam().entrySet().forEach( item -> {
            contentBuffer.append(item.getKey()).append(":").append(item.getValue()).append(",");
        });
        String content = contentBuffer.substring(0, contentBuffer.length() - 1);

        bodys.put("content", content);
        bodys.put("phone_number", smsVo.getPhone());
        bodys.put("template_id", smsVo.getTemplateCode());

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

            //获取response的body
            String data = EntityUtils.toString(response.getEntity());

            HashMap<String, String> resultMap = JSONObject.parseObject(data, HashMap.class);
            String status = resultMap.get("status");

            if(!"OK".equals(status)){
                String reason = resultMap.get("reason");
                log.error("短信发送失败：status = " + status + ", reason = " + reason);
                throw new GuiguException(ResultCodeEnum.FAIL.getCode(), "短信发送失败");
            }
            log.info("验证码短信发送成功");

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuiguException(ResultCodeEnum.FAIL.getCode(), "短信发送失败");
        }
    }
}