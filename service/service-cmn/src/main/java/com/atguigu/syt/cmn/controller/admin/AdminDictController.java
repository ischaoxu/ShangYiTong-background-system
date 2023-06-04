package com.atguigu.syt.cmn.controller.admin;


import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.DictService;
import com.atguigu.syt.vo.cmn.DictTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-06-04
 */
@Api(value = "数据字典")
@RestController
@RequestMapping("admin/cmn/dict")
public class AdminDictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("获取字典")
    @GetMapping("/findAllDictList")
    public Result<List<DictTypeVo>> listDict() {
        List<DictTypeVo> list = dictService.findAllDictList();
        return Result.ok(list);
    }
}

