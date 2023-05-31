package com.atguigu.syt.vo.cmn;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "数据字典类型VO")
public class DictTypeVo {

    @ApiModelProperty(value = "id")
    private String id;//加前缀确保前端的嵌套列表id的唯一性

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "下级")
    private List<DictVo> children;
}
