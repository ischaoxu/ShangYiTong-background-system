package com.atguigu.syt.hosp.controller.admin;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.Result;
import com.atguigu.common.util.tools.MD5;
import com.atguigu.syt.enums.GuiguExceptionEnum;
import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.model.hosp.HospitalSet;
import com.atguigu.syt.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class AdminHospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;


    @ApiOperation(value = "根据id查询医院设置")
    @GetMapping("/getHospSet/{id}")
    public Result getHosp(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }
    @ApiOperation(value = "根据id删除医院设置")
    @DeleteMapping("/{id}")
    public Result removeById(
            @ApiParam(value = "医院设置id",required = true)
            @PathVariable Long id){
        boolean remove = hospitalSetService.removeById(id);
        if(remove){
            return Result.ok().message("删除成功");
        }else{
            return Result.fail().message("删除失败");
        }

    }
    @ApiOperation(value = "新增医院设置")
    @PostMapping("/saveHospSet")
    public Result save(
            @ApiParam(value = "医院设置对象", required = true)
            @RequestBody HospitalSet hospitalSet) {
        hospitalSet.setStatus(1);
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+
                UUID.randomUUID().toString().replace("-","")));
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok().message("新增成功");
        } else {
            return Result.ok().message("新增失败");
        }
    }
    @ApiOperation(value = "根据ID修改医院设置")
    @PutMapping("/updateHospSet")
    public Result updateById(
            @ApiParam(value = "医院设置对象", required = true)
            @RequestBody HospitalSet hospitalSet){
        boolean update = hospitalSetService.updateById(hospitalSet);
        if(update){
            return Result.ok().message("修改成功");
        }else{
            return Result.fail().message("修改失败");
        }
    }
    @ApiOperation(value = "批量删除医院设置") //[1,2,3]
    @DeleteMapping("/batchRemove")
    public Result batchRemoveHospitalSet(
            @ApiParam(value = "id列表", required = true)
            @RequestBody List<Long> idList) {
        boolean remove = hospitalSetService.removeByIds(idList);
        if(remove){
            return Result.ok().message("删除成功");
        }else{
            return Result.fail().message("删除失败");
        }


    }
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(
            @ApiParam(value = "医院设置id",required = true) @PathVariable Long id,
            @ApiParam(value = "状态", required = true) @PathVariable Integer status) {
        if(status.intValue()!=0&&status.intValue()!=1){
            throw new GuiguException(
                    GuiguExceptionEnum.ILLEGAL_DATA.getStatusCode(),
                    GuiguExceptionEnum.ILLEGAL_DATA.getMsg());
        }
        HospitalSet hospitalSet = new HospitalSet();
        hospitalSet.setId(id);
        hospitalSet.setStatus(status);
        boolean update = hospitalSetService.updateById(hospitalSet);
        if(update){
            return Result.ok().message("操作成功");
        }else{
            return Result.fail().message("操作失败");
        }
    }
    @ApiOperation("分页条件查询")
    @GetMapping("/{page}/{limit}")
    public Result<Page> pageList(
            @ApiParam(value = "页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象",required = false)
            HospitalSetQueryVo hospitalSetQueryVo
    ){
        Page<HospitalSet> pageModel =
                hospitalSetService.selectPage(page,limit,hospitalSetQueryVo);
        return Result.ok(pageModel);
    }






}

