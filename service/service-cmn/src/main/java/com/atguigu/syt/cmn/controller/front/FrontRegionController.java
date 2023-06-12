package com.atguigu.syt.cmn.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuzhaoxu
 * @date 2023年06月12日 17:50
 */
@Api(tags = "地区接口")
@RestController
@RequestMapping("/front/cmn/region")
public class FrontRegionController {
    @Autowired
    private RegionService regionService;

    @ApiOperation(value = "根据上级code获取子节点数据列表")
    @ApiImplicitParam(name = "parentCode", value = "上级节点code", required = true)
    @GetMapping(value = "/findRegionList/{parentCode}")
    public Result<List<Region>> findRegionList(@PathVariable String parentCode) {
        List<Region> list = regionService.findRegionListByParentCode(parentCode);
        return Result.ok(list);
    }

}
