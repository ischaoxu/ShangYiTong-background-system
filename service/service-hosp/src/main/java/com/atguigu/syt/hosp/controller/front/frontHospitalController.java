package com.atguigu.syt.hosp.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuzhaoxu
 * @date 2023年06月12日 12:42
 */

@Api(tags = "医院接口")
@RestController
@RequestMapping("/front/hosp/hospital")
public class frontHospitalController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "根据医院名称、级别和区域查询医院列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hosname",value = "医院名称"),
            @ApiImplicitParam(name = "hostype",value = "医院类型"),
            @ApiImplicitParam(name = "districtCode",value = "医院地区")})
    @GetMapping("/list")
    public Result<List<Hospital>> showSearchHospital(String hosname, String hostype, String districtCode) {
        List<Hospital> hospitalList =  hospitalService.listHospitalSearch(hosname, hostype, districtCode);
        return Result.ok(hospitalList);
    }
}
