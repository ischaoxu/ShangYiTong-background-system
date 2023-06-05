package com.atguigu.syt.cmn.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
    @ApiOperation(value = "导出")
    @GetMapping("/exportData")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        List<RegionExcelVo> regionExcelVoList = regionService.findRegionExcelVoList();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据字典", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), RegionExcelVo.class).sheet("数据字典").doWrite(regionExcelVoList);

    }


    @ApiOperation(value = "导入")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        regionService.importData(file);
        return Result.ok();
    }

}
