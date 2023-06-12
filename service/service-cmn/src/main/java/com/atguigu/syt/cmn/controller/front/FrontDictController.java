package com.atguigu.syt.cmn.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.DictService;
import com.atguigu.syt.model.cmn.Dict;
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
 * @date 2023年06月12日 16:48
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/front/cmn/dict")
public class FrontDictController {

    @Autowired
    private DictService dictService;
    @ApiOperation(value = "根据数据字典类型id获取数据列表")
    @ApiImplicitParam(name = "dictTypeId", value = "类型id", required = true)
    @GetMapping(value = "/findDictList/{dictTypeId}")
    public Result<List<Dict>> showDictByDictTypeId(@PathVariable Long dictTypeId){
        List<Dict> list = dictService.listDictByDictTypeId(dictTypeId);
        return Result.ok(list);
    }


}
