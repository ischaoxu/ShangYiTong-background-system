package com.atguigu.syt.model.cmn;

import com.atguigu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典")
@TableName("dict")
public class Dict extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "name")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "value")
	@TableField("value")
	private String value;

	@ApiModelProperty(value = "dictTypeId")
	@TableField("dict_type_id")
	private Long dictTypeId;

}