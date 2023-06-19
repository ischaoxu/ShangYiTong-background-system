package com.atguigu.syt.hosp.controller.inner;

import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.vo.hosp.ScheduleOrderVo;
import com.atguigu.syt.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 20:10
 */
@Api(tags = "医院接口-供其他微服务远程调用")
@RestController
@RequestMapping("/inner/hosp/hospital")
public class InnerScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("根据排班id获取预约下单数据")
    @ApiImplicitParam(name = "scheduleId",value = "排班id", required = true)
    @GetMapping("/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

}