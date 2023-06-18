package com.atguigu.syt.cmn.controller.inner;

import com.atguigu.syt.cmn.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhaoxu
 * @date 2023年06月09日 18:05
 */

@Api(tags = "数据字典")
@RestController
@RequestMapping("/inner/cmn/dict")
@Slf4j
public class InnerDictController {
    @Autowired
    private DictService dictService;


    @ApiOperation(value = "获取数据字典名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypeId",value = "字典类型id", required = true),
            @ApiImplicitParam(name = "value",value = "字典值", required = true)})
    @GetMapping(value = "/getName/{dictTypeId}/{value}")
    public String getName(
            @PathVariable("dictTypeId") Long dictTypeId,
            @PathVariable("value") String value) {
//        log.info("睡眠:300ms");
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return dictService.getNameByDictTypeIdAndValue(dictTypeId, value);
    }





}
