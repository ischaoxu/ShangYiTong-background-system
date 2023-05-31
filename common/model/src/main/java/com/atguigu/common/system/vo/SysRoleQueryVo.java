//
//
package com.atguigu.common.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色查询实体
 */
@Data
public class SysRoleQueryVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String roleName;
}

