package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户兴趣标签实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_interests")
public class UserInterest extends BaseEntity {
    
    /**
     * 兴趣ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 兴趣分类
     */
    private String interestCategory;
    
    /**
     * 兴趣标签
     */
    private String interestTag;
    
    /**
     * 权重
     */
    private BigDecimal weight;
    
    /**
     * 来源：USER-用户设置，SYSTEM-系统推断
     */
    private String source;
}