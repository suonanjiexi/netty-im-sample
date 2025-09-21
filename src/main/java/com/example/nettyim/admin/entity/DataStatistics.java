package com.example.nettyim.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.nettyim.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据统计实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_statistics")
public class DataStatistics extends BaseEntity {
    
    /**
     * 统计ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 统计类型：user-用户统计, content-内容统计, payment-支付统计, membership-会员统计
     */
    @TableField("stat_type")
    private String statType;
    
    /**
     * 统计日期
     */
    @TableField("stat_date")
    private LocalDateTime statDate;
    
    /**
     * 新增用户数
     */
    @TableField("new_users")
    private Integer newUsers;
    
    /**
     * 活跃用户数
     */
    @TableField("active_users")
    private Integer activeUsers;
    
    /**
     * 总用户数
     */
    @TableField("total_users")
    private Integer totalUsers;
    
    /**
     * 新增内容数
     */
    @TableField("new_contents")
    private Integer newContents;
    
    /**
     * 总内容数
     */
    @TableField("total_contents")
    private Integer totalContents;
    
    /**
     * 新增订单数
     */
    @TableField("new_orders")
    private Integer newOrders;
    
    /**
     * 总订单数
     */
    @TableField("total_orders")
    private Integer totalOrders;
    
    /**
     * 订单总金额
     */
    @TableField("order_amount")
    private BigDecimal orderAmount;
    
    /**
     * 新增会员数
     */
    @TableField("new_members")
    private Integer newMembers;
    
    /**
     * 总会员数
     */
    @TableField("total_members")
    private Integer totalMembers;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}