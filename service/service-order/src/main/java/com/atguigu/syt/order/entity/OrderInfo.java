package com.atguigu.syt.order.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author com/atguigu
 * @since 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 订单交易号
     */
    private String outTradeNo;

    /**
     * 医院编号
     */
    private String hoscode;

    /**
     * 医院名称
     */
    private String hosname;

    /**
     * 科室编号
     */
    private String depcode;

    /**
     * 科室名称
     */
    private String depname;

    /**
     * 医生职称
     */
    private String title;

    /**
     * 排班编号（mongodb）
     */
    private String scheduleId;

    /**
     * 排班编号（医院自己的排班主键）
     */
    private String hosScheduleId;

    /**
     * 安排日期
     */
    private LocalDate reserveDate;

    /**
     * 安排时间（0：上午 1：下午）
     */
    private Integer reserveTime;

    /**
     * 就诊人id
     */
    private Long patientId;

    /**
     * 就诊人名称
     */
    private String patientName;

    /**
     * 就诊人手机
     */
    private String patientPhone;

    /**
     * 预约记录唯一标识（医院预约记录主键）
     */
    private String hosOrderId;

    /**
     * 预约号序
     */
    private Integer number;

    /**
     * 建议取号时间
     */
    private String fetchTime;

    /**
     * 取号地点
     */
    private String fetchAddress;

    /**
     * 医事服务费
     */
    private BigDecimal amount;

    /**
     * 退号时间
     */
    private LocalDateTime quitTime;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除(1:已删除，0:未删除)
     */
    private Integer isDeleted;


}
