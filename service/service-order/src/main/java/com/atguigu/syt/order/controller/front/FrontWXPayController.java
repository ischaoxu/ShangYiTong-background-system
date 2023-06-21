package com.atguigu.syt.order.controller.front;

import com.atguigu.common.service.utils.AuthUserVerify;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.order.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuzhaoxu
 * @date 2023年06月20日 11:18
 */

@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/front/order/wxpay")
public class FrontWXPayController {
    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private AuthUserVerify authUserVerify;

    @ApiOperation("获取支付二维码url")
    @ApiImplicitParam(name = "outTradeNo",value = "订单号", required = true)
    @GetMapping("/auth/nativePay/{outTradeNo}")
    public Result<String> nativePay(@PathVariable String outTradeNo, HttpServletRequest request, HttpServletResponse response) {

        //校验用户登录状态
        authUserVerify.checkAuth(request, response);

        String codeUrl = wxPayService.createNative(outTradeNo);
        return Result.ok(codeUrl);
    }

    @ApiOperation("查询支付状态")
    @ApiImplicitParam(name = "outTradeNo",value = "订单id", required = true)
    @GetMapping("/queryPayStatus/{outTradeNo}")
    public Result queryPayStatus(@PathVariable String outTradeNo) {
        //调用查询接口
        boolean success = wxPayService.queryPayStatus(outTradeNo);
        if (success) {
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中").code(250);
    }



}
