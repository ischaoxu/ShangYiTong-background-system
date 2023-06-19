package com.atguigu.syt.hosp.controller.front;

import com.atguigu.common.service.utils.AuthUserVerify;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.ScheduleService;
import com.atguigu.syt.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月19日 8:39
 */
@Api("挂号")
@RestController
@RequestMapping("/front/hosp/schedule")
public class FrontScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private AuthUserVerify authUserVerify;

    @ApiOperation("获取医院挂号周期")
    @ApiImplicitParams({@ApiImplicitParam(name = "hoscode", value = "医院编号"),
                         @ApiImplicitParam(name = "depcode", value = "科室编号")})
    @GetMapping("getBookingScheduleRule/{hoscode}/{depcode}")
    public Result<Map<String, Object>> showScheduleCycle(@PathVariable("hoscode") String hoscode,
                                                         @PathVariable("depcode") String depcode,
                                                         HttpServletResponse response,
                                                         HttpServletRequest request) {
        authUserVerify.checkAuth(request, response);
        Map<String,Object> map =  scheduleService.getScheduleCycleInfo(hoscode, depcode);
        return Result.ok(map);
    }
    @ApiOperation("获取排班数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hoscode",value = "医院编码", required = true),
            @ApiImplicitParam(name = "depcode",value = "科室编码", required = true),
            @ApiImplicitParam(name = "workDate",value = "排班日期", required = true)})
    @GetMapping("getScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result<List<Schedule>> getScheduleList(
            @PathVariable String hoscode,
            @PathVariable String depcode,
            @PathVariable String workDate,
            HttpServletResponse response,
            HttpServletRequest request) {
        authUserVerify.checkAuth(request, response);
        List<Schedule> scheduleList = scheduleService.listSchedule(hoscode, depcode, workDate);
        return Result.ok(scheduleList);
    }

    @ApiOperation("获取预约详情")
    @ApiImplicitParam(name = "id",value = "排班id", required = true)
    @GetMapping("getScheduleDetail/{id}")
    public Result<Schedule> getScheduleDetail(@PathVariable String id) {
        Schedule schedule = scheduleService.getDetailById(id);
        return Result.ok(schedule);
    }

}
