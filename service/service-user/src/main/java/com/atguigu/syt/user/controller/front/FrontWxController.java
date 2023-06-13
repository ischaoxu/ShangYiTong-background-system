package com.atguigu.syt.user.controller.front;

import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.user.utils.ConstantProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liuzhaoxu
 * @date 2023年06月12日 20:42
 */
@Api(tags = "微信扫码登录")
@Controller//注意这里没有配置 @RestController
@RequestMapping("/front/user/wx")
@Slf4j
public class FrontWxController {

    @Autowired
    private ConstantProperties constantProperties;

    @GetMapping("/login")
    public String login(HttpSession session){
//        根据微信开放平台官网生成二维码
//        生成state存入session用来防止 CSRF攻击
        try {
            long nonce = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
            //十六进制表示的随机数
            String state = Long.toHexString(nonce);
            session.setAttribute("wx_oauth_state",state);
//        模板:
            //https://open.weixin.qq.com/connect/qrconnect?
            // appid=APPID
            // &redirect_uri=REDIRECT_URI
            // &response_type=code
            // &scope=SCOPE
            // &state=STATE
            // #wechat_redirect

            StringBuffer baseUrl = new StringBuffer("https://open.weixin.qq.com/connect/qrconnect?");
            baseUrl.append("appid="+constantProperties.getAppId());
//              处理回调的请求编码格式为utf8
            baseUrl.append("&redirect_uri=" + URLEncoder.encode(constantProperties.getRedirectUri(),"UTF-8"));
            baseUrl.append("&response_type=code");
            baseUrl.append("&scope=snsapi_login");
            baseUrl.append("&state="+state);
            baseUrl.append("#wechat_redirect");
            return "redirect:"+baseUrl;
        } catch (Exception e) {
            throw new GuiguException(ResultCodeEnum.URL_ENCODE_ERROR, e);
        }
    }
}