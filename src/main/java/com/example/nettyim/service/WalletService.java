package com.example.nettyim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.UserWallet;
import com.example.nettyim.entity.WalletTransaction;

import java.math.BigDecimal;

/**
 * 钱包服务接口
 */
public interface WalletService {
    
    /**
     * 获取用户钱包
     */
    UserWallet getUserWallet(Long userId);
    
    /**
     * 创建用户钱包
     */
    Boolean createUserWallet(Long userId);
    
    /**
     * 充值
     */
    Boolean recharge(Long userId, BigDecimal amount, String relatedOrderNo, String description);
    
    /**
     * 消费
     */
    Boolean consume(Long userId, BigDecimal amount, String relatedOrderNo, String description);
    
    /**
     * 退款
     */
    Boolean refund(Long userId, BigDecimal amount, String relatedOrderNo, String description);
    
    /**
     * 冻结资金
     */
    Boolean freezeAmount(Long userId, BigDecimal amount, String relatedOrderNo, String description);
    
    /**
     * 解冻资金
     */
    Boolean unfreezeAmount(Long userId, BigDecimal amount, String relatedOrderNo, String description);
    
    /**
     * 转账（转出）
     */
    Boolean transferOut(Long userId, BigDecimal amount, String relatedOrderNo, String description);
    
    /**
     * 转账（转入）
     */
    Boolean transferIn(Long userId, BigDecimal amount, String relatedOrderNo, String description);
    
    /**
     * 设置支付密码
     */
    Boolean setPaymentPassword(Long userId, String password);
    
    /**
     * 验证支付密码
     */
    Boolean verifyPaymentPassword(Long userId, String password);
    
    /**
     * 锁定钱包
     */
    Boolean lockWallet(Long userId, String reason);
    
    /**
     * 解锁钱包
     */
    Boolean unlockWallet(Long userId);
    
    /**
     * 获取用户交易记录
     */
    IPage<WalletTransaction> getUserTransactions(Long userId, Page<WalletTransaction> page);
    
    /**
     * 获取用户余额
     */
    BigDecimal getUserBalance(Long userId);
    
    /**
     * 检查余额是否足够
     */
    Boolean hasEnoughBalance(Long userId, BigDecimal amount);
}