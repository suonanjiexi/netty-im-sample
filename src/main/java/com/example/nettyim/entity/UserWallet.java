package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户钱包实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_wallets")
public class UserWallet extends BaseEntity {
    
    /**
     * 钱包ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 余额
     */
    private BigDecimal balance;
    
    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;
    
    /**
     * 累计充值
     */
    private BigDecimal totalRecharge;
    
    /**
     * 累计消费
     */
    private BigDecimal totalConsumption;
    
    /**
     * 支付密码
     */
    private String paymentPassword;
    
    /**
     * 是否锁定：0-正常，1-锁定
     */
    private Boolean isLocked;
    
    /**
     * 锁定原因
     */
    private String lockReason;
    
    /**
     * 锁定时间
     */
    private LocalDateTime lockTime;
    
    /**
     * 获取可用余额
     */
    public BigDecimal getAvailableBalance() {
        BigDecimal totalBalance = balance != null ? balance : BigDecimal.ZERO;
        BigDecimal frozen = frozenAmount != null ? frozenAmount : BigDecimal.ZERO;
        return totalBalance.subtract(frozen);
    }
    
    /**
     * 检查余额是否足够
     */
    public Boolean hasEnoughBalance(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        
        return getAvailableBalance().compareTo(amount) >= 0;
    }
    
    /**
     * 检查钱包是否可用
     */
    public Boolean isAvailable() {
        return !Boolean.TRUE.equals(isLocked);
    }
    
    /**
     * 检查是否设置了支付密码
     */
    public Boolean hasPaymentPassword() {
        return paymentPassword != null && !paymentPassword.trim().isEmpty();
    }
}