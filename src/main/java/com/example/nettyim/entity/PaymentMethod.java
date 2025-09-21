package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 支付方式实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment_methods")
public class PaymentMethod extends BaseEntity {
    
    /**
     * 支付方式ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 支付方式代码
     */
    private String methodCode;
    
    /**
     * 支付方式名称
     */
    private String methodName;
    
    /**
     * 支付类型：1-第三方支付，2-银行卡，3-余额支付
     */
    private Integer methodType;
    
    /**
     * 图标URL
     */
    private String iconUrl;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 配置信息（JSON格式）
     */
    private String configJson;
    
    /**
     * 手续费率
     */
    private BigDecimal feeRate;
    
    /**
     * 最小支付金额
     */
    private BigDecimal minAmount;
    
    /**
     * 最大支付金额
     */
    private BigDecimal maxAmount;
    
    /**
     * 是否启用
     */
    private Boolean isActive;
    
    /**
     * 显示顺序
     */
    private Integer sortOrder;
    
    /**
     * 计算手续费
     */
    public BigDecimal calculateFee(BigDecimal amount) {
        if (amount == null || feeRate == null) {
            return BigDecimal.ZERO;
        }
        
        return amount.multiply(feeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * 检查金额是否在支付范围内
     */
    public boolean isAmountValid(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        
        if (minAmount != null && amount.compareTo(minAmount) < 0) {
            return false;
        }
        
        if (maxAmount != null && amount.compareTo(maxAmount) > 0) {
            return false;
        }
        
        return true;
    }
}