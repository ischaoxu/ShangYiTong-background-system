package com.atguigu.syt.vo.cmn;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class RegionExcelVo {

	@ExcelProperty(value = "id" ,index = 0)
	private Long id;

	@ExcelProperty(value = "上级code" ,index = 1)
	private String parentCode;

	@ExcelProperty(value = "地区名称" ,index = 2)
	private String name;

	@ExcelProperty(value = "地区编码" ,index = 3)
	private String code;

	@ExcelProperty(value = "地区级别" ,index = 4)
	private Integer level;

}

