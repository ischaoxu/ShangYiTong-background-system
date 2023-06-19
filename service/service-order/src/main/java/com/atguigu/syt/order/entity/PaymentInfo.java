package com.atguigu.syt.order.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 支付信息表
 * </p>
 *
 * @author com/atguigu
 * @since 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对外业务编号
     */
    private String outTradeNo;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 支付类型（微信 支付宝）
     */
    private Boolean paymentType;

    /**
     * 交易编号
     */
    private String tradeNo;

    /**
     * 支付金额
     */
    private BigDecimal totalAmount;

    /**
     * 交易内容
     */
    private String subject;

    /**
     * 支付状态
     */
    private Integer paymentStatus;

    /**
     * 回调时间
     */
    private LocalDateTime callbackTime;

    /**
     * 回调信息
     */
    private String callbackContent;

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
