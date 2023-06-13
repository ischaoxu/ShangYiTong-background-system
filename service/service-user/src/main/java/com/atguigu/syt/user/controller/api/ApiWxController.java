package com.atguigu.syt.user.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.service.utils.HttpUtil;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.enums.UserStatusEnum;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.user.utils.ConstantProperties;
import com.atguigu.syt.user.utils.CookieUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhaoxu
 * @date 2023年06月12日 20:45
 */

@Api(tags = "微信扫码登录回调")
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/user/wx")
@Slf4j
public class ApiWxController {
    @Resource
    private ConstantProperties constantProperties;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 登录回调
     * @param code
     * @param state
     * @param session
     * @return
     */
    @GetMapping("/callback")
    public String callback(String code, String state, HttpSession session, HttpServletResponse response) {
        try {
//   官方文档:
//   PC端会跳转到 https://test.yhd.com/wechat/callback.do?code=CODE&state=3d6be0a40sssssxxxxx6624a415e
//        1,进行非空判断,验证state是否一致防止CSRF
            String sessionState = (String) session.getAttribute("wx_oauth_state");
            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state) || !sessionState.equals(state)) {
                log.info("非法参数:code="+code+";state="+state+";sessionState="+sessionState);
                throw new GuiguException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
//        2,通code加appid等调用api获取access_token和openid
//        官方模板:
//        https://api.weixin.qq.com/sns/oauth2/access_token?
//        appid=APPID
//        &secret=SECRET
//        &code=CODE
//        &grant_type=authorization_code
            StringBuffer baseUrl = new StringBuffer(" https://api.weixin.qq.com/sns/oauth2/access_token?");
            baseUrl.append("appid=" + constantProperties.getAppId());
            baseUrl.append("&secret=" + constantProperties.getAppSecret());
            baseUrl.append("&code=" + code);
            baseUrl.append("&grant_type=authorization_code");
            log.info("通code加appid等调用api获取access_token和openid"+baseUrl);
//        3,判断用户是否注册
            String repString = new String(HttpUtil.doGet(baseUrl.toString()));
            JSONObject resultObject = JSONObject.parseObject(repString);
            if (resultObject.getString("errcode") != null) {
                log.error("获取access_token失败：" + resultObject.getString("errcode") + resultObject.getString("errmsg"));
                throw new GuiguException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
            }
            String accessToken = resultObject.getString("access_token");
            String openid = resultObject.getString("openid");
//查询数据库中用户信息
            UserInfo userInfo = userInfoService.getByOpenId(openid);

            if (userInfo != null) {
                //        4,注册用户直接判断用户状态
                log.info("判断用户是否被禁用");
                if (userInfo.getStatus().intValue() == UserStatusEnum.LOCK.getStatus().intValue()) {
                    log.error("用户已被禁用");
                    throw new GuiguException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
                }
            } else {
                //        5,未注册用户通过access_token和openid调用api查询用户微信信息就行注册
                //            http请求方式: GET
                //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
                String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                        "access_token=" + accessToken +
                        "&openid=" + openid;
                log.info("userInfoUrl获取微信用户信息=="+userInfoUrl);
                //            5.1未注册用户信息获取
                String resultStr = new String(HttpUtil.doGet(userInfoUrl));
                JSONObject jsonObject = JSONObject.parseObject(resultStr);
                log.info("用户微信信息:"+jsonObject.toJSONString());
                //            5.2设置信息及状态
                userInfo = new UserInfo();
                userInfo.setNickName(jsonObject.getString("nickname"));
                userInfo.setOpenid(jsonObject.getString("openid"));
                userInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                userInfo.setStatus(UserStatusEnum.NORMAL.getStatus());
                userInfoService.save(userInfo);
            }
//        封装用户名称,未实名认证暂时使用昵称代替
            if (StringUtils.isEmpty(userInfo.getName())) {
                userInfo.setName(userInfo.getNickName());
            }
//        生成令牌
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            log.info("生成令牌="+token);
            redisTemplate.opsForValue().set("user:token:" + token, userInfo.getId(), 30, TimeUnit.MINUTES);
            int cookieMaxTime = 60 * 30;
            CookieUtils.setCookie(response, "token", token, cookieMaxTime);
            CookieUtils.setCookie(response, "name", URLEncoder.encode(userInfo.getName()), cookieMaxTime);
            CookieUtils.setCookie(response, "headimgurl", URLEncoder.encode(userInfo.getHeadImgUrl()), cookieMaxTime);
            return "redirect:" + constantProperties.getSytBaseUrl();
        } catch (GuiguException e) {
            return "redirect:" + constantProperties.getSytBaseUrl() + "?code=201&message=" + URLEncoder.encode(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + constantProperties.getSytBaseUrl()
                    + "?code=201&message="+URLEncoder.encode("登录失败");
        }
    }
}