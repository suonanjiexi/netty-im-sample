package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户签到记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_check_ins")
public class UserCheckIn extends BaseEntity {
    
    /**
     * 签到ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 签到日期
     */
    private LocalDate checkInDate;
    
    /**
     * 连续签到天数
     */
    private Integer continuousDays;
    
    /**
     * 获得积分
     */
    private Integer pointsEarned;
    
    /**
     * 奖励类型
     */
    private String bonusType;
    
    /**
     * 奖励金额
     */
    private BigDecimal bonusAmount;
}