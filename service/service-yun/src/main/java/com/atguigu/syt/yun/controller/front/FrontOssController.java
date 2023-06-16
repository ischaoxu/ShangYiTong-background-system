package com.atguigu.syt.yun.controller.front;

import com.atguigu.common.service.utils.AuthUserVerify;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.yun.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author liuzhaoxu
 * @date 2023年06月14日 12:56
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/front/yun/file")
public class FrontOssController {

    @Autowired
    private OssService ossService;

    @Autowired
    private AuthUserVerify authUserVerify;

    @ApiOperation("文件上传")
    @ApiImplicitParam(name = "file", value = "上传文件", required = true)
    @PostMapping("/auth/upload")
    public Result<Map<String, String>> upload(HttpServletRequest request,MultipartFile file) {
        //        权限校验
        authUserVerify.checkAuth(request);
        Map<String, String> map = ossService.uploadFile(file);
        return Result.ok(map);
    }
}
