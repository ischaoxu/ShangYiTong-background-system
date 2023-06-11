package com.atguigu.syt.hosp.controller.admin;

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

import java.util.List;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月11日 17:52
 */
@Api(tags = "排班接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class AdminScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value ="查询排班规则数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "limit",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true),
            @ApiImplicitParam(name = "depcode",value = "部门编码",required = true)
    })
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Map<String,Object>> getScheduleRule(@PathVariable Long page,
                                       @PathVariable Long limit,
                                       @PathVariable String hoscode,
                                       @PathVariable String depcode) {
        Map<String,Object> map =   scheduleService.getSchedulePage(page, limit, hoscode, depcode);
        return Result.ok(map);
    }
    @ApiOperation(value = "查询排班详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hoscode",value = "医院编码",required = true),
            @ApiImplicitParam(name = "depcode",value = "部门编码",required = true),
            @ApiImplicitParam(name = "workDate",value = "选择日期",required = true)
    })
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result<List<Schedule>> getScheduleDetail(@PathVariable String hoscode,
                                                    @PathVariable String depcode,
                                                    @PathVariable String workDate) {

        List<Schedule> scheduleList =scheduleService.listSchedule(hoscode,depcode,workDate);
        return Result.ok(scheduleList);

    }

}
