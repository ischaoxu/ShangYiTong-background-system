package com.atguigu.syt.order.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.enums.OrderStatusEnum;
import com.atguigu.syt.enums.RefundStatusEnum;
import com.atguigu.syt.order.service.OrderInfoService;
import com.atguigu.syt.order.service.RefundInfoService;
import com.atguigu.syt.order.utils.RequestUtils;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import com.wechat.pay.java.service.refund.model.Status;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月21日 15:30
 */
@Api(tags = "微信回调接口")
@RestController
@RequestMapping("/api/order/wxpay")
@Slf4j
public class WXPayController {

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private RefundInfoService refundInfoService;

    @Autowired
    private RSAAutoCertificateConfig rsaAutoCertificateConfig;
    @PostMapping("/refunds/notify")
    public String callback(HttpServletRequest request, HttpServletResponse response){

        log.info("退款通知执行");

        Map<String, String> map = new HashMap<>();//应答对象

        try {

             /*使用回调通知请求的数据，构建 RequestParam。
            HTTP 头 Wechatpay-Signature
            HTTP 头 Wechatpay-Nonce
            HTTP 头 Wechatpay-Timestamp
            HTTP 头 Wechatpay-Serial
            HTTP 头 Wechatpay-Signature-Type
            HTTP 请求体 body。切记使用原始报文，不要用 JSON 对象序列化后的字符串，避免验签的 body 和原文不一致。*/
            // 构造 RequestParam
            String signature = request.getHeader("Wechatpay-Signature");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String wechatPayCertificateSerialNumber = request.getHeader("Wechatpay-Serial");

            //请求体
            String requestBody = RequestUtils.readData(request);

            RequestParam requestParam = new com.wechat.pay.java.core.notification.RequestParam.Builder()
                    .serialNumber(wechatPayCertificateSerialNumber)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(requestBody)
                    .build();

            // 初始化 NotificationParser
            NotificationParser parser = new NotificationParser(rsaAutoCertificateConfig);

            // 验签、解密并转换成 Transaction
            RefundNotification refundNotification = parser.parse(requestParam, RefundNotification.class);

            String orderTradeNo = refundNotification.getOutTradeNo();
            Status refundStatus = refundNotification.getRefundStatus();

            if("SUCCESS".equals(refundStatus.toString())){
                log.info("更新退款记录：已退款");
                //退款状态
                refundInfoService.updateRefundInfoStatus(refundNotification, RefundStatusEnum.REFUND);
                //订单状态
                orderInfoService.updateStatus(orderTradeNo, OrderStatusEnum.CANCLE_REFUND.getStatus());
            }

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            return JSONObject.toJSONString(map);

        } catch (Exception e) {

            log.error(ExceptionUtils.getStackTrace(e));

            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return JSONObject.toJSONString(map);
        }
    }

}
