package com.atguigu.syt.vo.hosp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "可预约排班规则数据")
public class ScheduleRuleVo {
	
	@ApiModelProperty(value = "可预约日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date workDate;

	@ApiModelProperty(value = "周几")
	private String dayOfWeek;

	@ApiModelProperty(value = "科室可预约数")
	private Integer reservedNumber;

	@ApiModelProperty(value = "科室剩余预约数")
	private Integer availableNumber;
}

