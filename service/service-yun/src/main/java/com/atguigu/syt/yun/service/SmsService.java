package com.atguigu.syt.yun.service;

import com.atguigu.syt.vo.sms.SmsVo;

/**
 * @author liuzhaoxu
 * @date 2023年06月21日 18:02
 */
public interface SmsService {
    /**
     * 发送短信
     * @param smsVo
     */
    void send(SmsVo smsVo);
}