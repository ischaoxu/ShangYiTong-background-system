package com.atguigu.syt.hosp.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class HospitalTest {

    @Autowired
    private HospitalService hospitalService;

    @Value("classpath:hospital.json")
    private Resource hospitalResource;

    @Test
    public void  saveBatchHospital() throws IOException {

        File file = hospitalResource.getFile();
        String jsonData = this.jsonRead(file);
        JSONArray jsonArray = JSONArray.parseArray(jsonData);
        for(int i=1, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("status",1);
            paramMap.put("hoscode","1000_"+i);
            paramMap.put("hosname",jsonObject.getString("hosname"));
            paramMap.put("hostype",i % 3 == 0 ? 1 : i % 3 == 1 ? 2 :4);
            paramMap.put("provinceCode","110000");
            paramMap.put("cityCode", "110100");
            if(i % 3 == 0) {
                paramMap.put("districtCode","110101");
            }
            if(i % 3 == 1) {
                paramMap.put("districtCode","110102");
            }
            if(i % 3 == 2) {
                paramMap.put("districtCode","110106");
            }

            paramMap.put("address","");
            String intro = jsonObject.getString("hosname")+"是集医疗、教学、科研于一体的大型三级甲等综合医院，是国家卫生计生委指定的全国疑难重症诊治指导中心，也是最早承担高干保健和外宾医疗任务的医院之一，以学科齐全、技术力量雄厚、特色专科突出、多学科综合优势强大享誉海内外。";
            paramMap.put("intro",intro);
            String route = "\n" +
                    "\n" +
                    "顺12、14、18、19、28、31、34、38、39、40路到中医院；顺2、11、15、16、17、20、21、22、23、24、25、26、27、29、32、33、36、37、41、43、45路、945到国泰下车，向南行300米即到" +
                    "\n";
            paramMap.put("route",route);
            //logo
            paramMap.put("logoData", this.getImgStr(jsonObject.getString("picture")));

            Map<String, Object> bookingRuleMap = new HashMap<>();
            bookingRuleMap.put("cycle",10);
            bookingRuleMap.put("releaseTime",jsonObject.getString("releaseTime"));
            bookingRuleMap.put("stopTime","12:30");
            bookingRuleMap.put("quitDay",-1);
            bookingRuleMap.put("quitTime","15:30");
            bookingRuleMap.put("rule","[\"西院区预约号取号地点：西院区门诊楼一层大厅挂号窗口取号\"]");
            paramMap.put("bookingRule",JSONObject.toJSONString(bookingRuleMap));

            log.info(JSON.toJSONString(paramMap));
            hospitalService.save(paramMap,(String) paramMap.get("hoscode"));
        }
    }

    private String jsonRead(File file){
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }

    public static String getImgStr(String imgFile){
        byte[] data = getImageBytes(imgFile);
        return new String(Base64.encodeBase64(data));
    }

    public static byte[] getImageBytes(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return outStream.toByteArray();
        } catch (ConnectException e) {
            log.error("获取图片时连接异常，url:{}",imgUrl,e);
        } catch (MalformedURLException e) {
            log.error("url出现异常，url:{}",imgUrl,e);
        } catch (IOException e) {
            log.error("读取图片时发生异常，url:{}",imgUrl,e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return null;
    }

}
