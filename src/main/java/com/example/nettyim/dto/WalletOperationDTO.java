package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 钱包操作DTO
 */
@Data
public class WalletOperationDTO {
    
    /**
     * 操作类型：RECHARGE-充值，CONSUME-消费，TRANSFER-转账
     */
    @NotNull(message = "操作类型不能为空")
    private String operationType;
    
    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须大于0")
    private BigDecimal amount;
    
    /**
     * 目标用户ID（转账时使用）
     */
    private Long targetUserId;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 支付密码（消费和转账时需要）
     */
    private String paymentPassword;
}