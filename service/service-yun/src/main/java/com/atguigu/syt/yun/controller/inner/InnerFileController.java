package com.atguigu.syt.yun.controller.inner;

import com.atguigu.syt.yun.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liuzhaoxu
 * @date 2023年06月14日 20:58
 */

@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/inner/yun/file")
@Slf4j
public class InnerFileController {

    @Resource
    private OssService ossService;

    @ApiOperation(value = "获取图片预览Url")
    @ApiImplicitParam(name = "objectName",value = "文件名", required = true)
    @GetMapping("/getPreviewUrl")
    public String getPreviewUrl(@RequestParam String objectName) {
        log.info("睡眠:1000ms");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ossService.getPreviewUrl(objectName);
    }
}