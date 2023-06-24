package com.atguigu.syt.yun.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.vo.sms.SmsVo;
import com.atguigu.syt.yun.service.SmsService;
import com.atguigu.syt.yun.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhaoxu
 * @date 2023年06月21日 18:01
 */

@Api(tags = "短信接口")
@RestController
@RequestMapping("/front/yun/sms")
public class FrontSmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("发送短信")
    @ApiImplicitParam(name = "phone",value = "手机号")
    @PostMapping("/send/{phone}")
    public Result send(@PathVariable String phone) {

        //生成验证码
        int minutes = 5; //验证码5分钟有效
        String code = RandomUtil.getFourBitRandom();

        //创建短信发送对象
        SmsVo smsVo = new SmsVo();
        smsVo.setPhone(phone);
        smsVo.setTemplateCode("CST_qozfh101");
        Map<String,Object> paramsMap = new HashMap<String, Object>(){{
            put("code", code);
            put("expire_at", 5);
        }};
        smsVo.setParam(paramsMap);

        //发送短信
        smsService.send(smsVo);

        //验证码存入redis
        redisTemplate.opsForValue().set("code:" + phone, code, minutes, TimeUnit.MINUTES);

        return Result.ok().message("短信发送成功");
    }
}