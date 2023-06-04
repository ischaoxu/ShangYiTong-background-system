package com.atguigu.syt.cmn.controller.admin;


import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-06-04
 */
@Api(tags = "地区")
@RestController
@RequestMapping("/admin/cmn/region")
public class AdminRegionController {

    @Resource
    private RegionService regionService;

    @ApiOperation(value = "根据上级code获取子节点数据列表")
    @ApiImplicitParam(name = "parentCode", value = "上级节点code", required = true)
    @GetMapping(value = "/findRegionListByParentCode/{parentCode}")
    public Result<List<Region>> findRegionListByParentCode(
            @PathVariable("parentCode") String parentCode) {
        List<Region> list = regionService.findRegionListByParentCode(parentCode);
        return Result.ok(list);
    }
}
