package com.atguigu.syt.model.cmn;

import com.atguigu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典类型")
@TableName("dict_type")
public class DictType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "name")
    @TableField("name")
    private String name;

}