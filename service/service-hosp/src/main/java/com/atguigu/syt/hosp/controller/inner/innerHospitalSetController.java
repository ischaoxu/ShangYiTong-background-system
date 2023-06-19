package com.atguigu.syt.hosp.controller.inner;

import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhaoxu
 * @date 2023年06月12日 12:42
 */

@Api(tags = "医院接口")
@RestController
@RequestMapping("/inner/hosp/hospital")
public class innerHospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    @GetMapping("/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(@PathVariable("hoscode") String hoscode) {
     return   hospitalSetService.getSinInfovo(hoscode);
    }
}
