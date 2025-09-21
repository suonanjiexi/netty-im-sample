package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会员积分记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("membership_points")
public class MembershipPoint extends BaseEntity {
    
    /**
     * 积分记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 积分类型：1-获得，2-消费
     */
    private Integer pointsType;
    
    /**
     * 积分数量
     */
    private Integer pointsAmount;
    
    /**
     * 来源类型：登录、充值、消费、推荐等
     */
    private String sourceType;
    
    /**
     * 来源ID（如订单ID、推荐ID等）
     */
    private Long sourceId;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 操作后余额
     */
    private Integer balanceAfter;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 检查积分是否过期
     */
    public Boolean isExpired() {
        return expireTime != null && expireTime.isBefore(LocalDateTime.now());
    }
}