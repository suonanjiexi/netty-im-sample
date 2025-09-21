package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment_orders")
public class PaymentOrder extends BaseEntity {
    
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 产品类型：1-会员升级，2-会员续费，3-积分充值，4-虚拟物品
     */
    private Integer productType;
    
    /**
     * 产品ID（如会员等级ID）
     */
    private Long productId;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 产品描述
     */
    private String productDescription;
    
    /**
     * 原始金额
     */
    private BigDecimal originalAmount;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 实际支付金额
     */
    private BigDecimal finalAmount;
    
    /**
     * 支付方式ID
     */
    private Long paymentMethodId;
    
    /**
     * 支付状态：0-待支付，1-支付中，2-支付成功，3-支付失败，4-已退款，5-已取消
     */
    private Integer paymentStatus;
    
    /**
     * 第三方交易号
     */
    private String tradeNo;
    
    /**
     * 支付时间
     */
    private LocalDateTime paidTime;
    
    /**
     * 支付回调数据
     */
    private String callbackData;
    
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    
    /**
     * 退款原因
     */
    private String refundReason;
    
    /**
     * 订单过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 检查订单是否过期
     */
    public Boolean isExpired() {
        return expireTime != null && expireTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * 检查订单是否可以支付
     */
    public Boolean canPay() {
        return paymentStatus != null && paymentStatus == 0 && !isExpired();
    }
    
    /**
     * 检查订单是否已支付
     */
    public Boolean isPaid() {
        return paymentStatus != null && paymentStatus == 2;
    }
    
    /**
     * 检查订单是否可以退款
     */
    public Boolean canRefund() {
        return isPaid() && (refundAmount == null || refundAmount.compareTo(finalAmount) < 0);
    }
    
    /**
     * 生成订单号
     */
    public static String generateOrderNo() {
        return "PO" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
}