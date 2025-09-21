package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 钱包交易记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wallet_transactions")
public class WalletTransaction extends BaseEntity {
    
    /**
     * 交易记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 交易号
     */
    private String transactionNo;
    
    /**
     * 交易类型：1-充值，2-消费，3-退款，4-冻结，5-解冻，6-转入，7-转出
     */
    private Integer transactionType;
    
    /**
     * 交易金额
     */
    private BigDecimal amount;
    
    /**
     * 交易前余额
     */
    private BigDecimal balanceBefore;
    
    /**
     * 交易后余额
     */
    private BigDecimal balanceAfter;
    
    /**
     * 关联订单号
     */
    private String relatedOrderNo;
    
    /**
     * 交易描述
     */
    private String description;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 生成交易号
     */
    public static String generateTransactionNo() {
        return "WT" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
    
    /**
     * 检查是否为收入交易
     */
    public Boolean isIncome() {
        return transactionType != null && (transactionType == 1 || transactionType == 3 || transactionType == 5 || transactionType == 6);
    }
    
    /**
     * 检查是否为支出交易
     */
    public Boolean isExpense() {
        return transactionType != null && (transactionType == 2 || transactionType == 4 || transactionType == 7);
    }
}