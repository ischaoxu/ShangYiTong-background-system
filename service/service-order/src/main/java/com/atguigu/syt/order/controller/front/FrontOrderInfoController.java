package com.atguigu.syt.order.controller.front;

import com.atguigu.common.service.utils.AuthUserVerify;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.model.order.OrderInfo;
import com.atguigu.syt.order.service.OrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 19:57
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/front/order/orderInfo")
public class FrontOrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private AuthUserVerify authUserVerify;

    @ApiOperation("创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scheduleId",value = "排班id", required = true),
            @ApiImplicitParam(name = "patientId",value = "就诊人id", required = true)})
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result<Long> submitOrder(@PathVariable String scheduleId,
                                    @PathVariable Long patientId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        authUserVerify.checkAuth(request, response);
        Long orderId = orderInfoService.generateOrder(scheduleId, patientId);
        return Result.ok(orderId);
    }
    @ApiOperation("根据订单id查询订单详情")
    @ApiImplicitParam(name = "orderId",value = "订单id", required = true)
    @GetMapping("/auth/getOrder/{orderId}")
    public Result<OrderInfo> getOrder(@PathVariable Long orderId, HttpServletRequest request, HttpServletResponse response) {
        authUserVerify.checkAuth(request, response);
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderId);
        return Result.ok(orderInfo);
    }
    @ApiOperation("订单列表")
    @GetMapping("/auth/list")
    public Result<List<OrderInfo>> list(HttpServletRequest request, HttpServletResponse response) {
        Long userId = authUserVerify.checkAuth(request, response);
        List<OrderInfo> orderInfolist = orderInfoService.selectList(userId);
        return Result.ok(orderInfolist);
    }

}
