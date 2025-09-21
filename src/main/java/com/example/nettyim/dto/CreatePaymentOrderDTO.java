package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 创建支付订单DTO
 */
@Data
public class CreatePaymentOrderDTO {
    
    /**
     * 产品类型：1-会员升级，2-会员续费，3-积分充值，4-虚拟物品
     */
    @NotNull(message = "产品类型不能为空")
    private Integer productType;
    
    /**
     * 产品ID（如会员等级ID）
     */
    private Long productId;
    
    /**
     * 产品名称
     */
    @NotNull(message = "产品名称不能为空")
    private String productName;
    
    /**
     * 产品描述
     */
    private String productDescription;
    
    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须大于0")
    private BigDecimal amount;
    
    /**
     * 支付方式代码
     */
    @NotNull(message = "支付方式不能为空")
    private String paymentMethodCode;
    
    /**
     * 续费类型（仅会员相关产品需要）：1-月费，2-年费
     */
    private Integer renewType;
}