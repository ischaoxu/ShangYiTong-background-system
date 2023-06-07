package com.atguigu.common.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.common.util.tools.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class HttpRequestHelper {

    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        //id=1，username=张三，tel=13012345678
        paramMap.put("id", "1");
        paramMap.put("username", "张三");
        paramMap.put("tel", "13012345678");
        paramMap.put("timestamp", getTimestamp());
        log.info(getSign(paramMap, "1234567890"));
//       checkSign(paramMap, "1234567890");
    }

    /**
     *
     * @param paramMap
     * @return
     */
    public static Map<String, Object> switchMap(Map<String, String[]> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            resultMap.put(param.getKey(), param.getValue()[0]);
        }
        return resultMap;
    }

    /**
     * 请求数据获取签名
     * @param paramMap
     * @param signKey
     * @return
     */
    public static String getSign(Map<String, Object> paramMap, String signKey) {
        if(paramMap.containsKey("sign")) {
            paramMap.remove("sign");
        }
        TreeMap<String, Object> sorted = new TreeMap<>(paramMap);
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Object> param : sorted.entrySet()) {
            str.append(param.getValue()).append("|");
        }
        str.append(signKey);
        log.info("加密前：" + str);
        String md5Str = MD5.encrypt(str.toString());
        log.info("加密后：" + md5Str);
        return md5Str;
    }

    /**
     * 签名校验
     * @param parameterMap
     */
    public static void checkSign(Map<String, Object> parameterMap, String signKey){
        //校验签名时间
        String remoteTimestamp = (String)parameterMap.get("timestamp");
        if(StringUtils.isEmpty(remoteTimestamp)){
            throw new GuiguException(ResultCodeEnum.SIGN_ERROR);
        }

        long currentTimestamp = getTimestamp();
        if (Math.abs(currentTimestamp - Long.parseLong(remoteTimestamp)) > 500000) {
            log.error("签名已过期，服务器当前时间:{}", currentTimestamp);
            throw new GuiguException(ResultCodeEnum.SIGN_OVERDUE);
        }

        //校验签名
        String remoteSign = (String)parameterMap.get("sign");
        if(StringUtils.isEmpty(remoteSign)){
            log.error("没有签名");
            throw new GuiguException(ResultCodeEnum.SIGN_NOT_EXIST);
        }

        String localSign = getSign(parameterMap, signKey);
        if(!remoteSign.equals(localSign)){
            log.error("验签失败");
            throw new GuiguException(ResultCodeEnum.SIGN_ERROR);
        }
    }

    /**
     * 获取时间戳
     * @return
     */
    public static long getTimestamp() {
        return new Date().getTime();
    }

    /**
     * 封装同步请求
     * @param paramMap
     * @param url
     * @return
     */
    public static JSONObject sendRequest(Map<String, Object> paramMap, String url){
        String result = "";
        try {
            //封装post参数
            StringBuilder postdata = new StringBuilder();
            for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                postdata.append(param.getKey()).append("=")
                        //对http传输中的特殊字符进行转换
                        .append(URLEncoder.encode(param.getValue().toString(),"utf-8")).append("&");
            }
            log.info(String.format("--> 发送请求：post data %1s", postdata));
            byte[] reqData = postdata.toString().getBytes("utf-8");
            byte[] respdata = HttpUtil.doPost(url,reqData);
            result = new String(respdata);
            log.info(String.format("--> 应答结果：result data %1s", result));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.parseObject(result);
    }
}
