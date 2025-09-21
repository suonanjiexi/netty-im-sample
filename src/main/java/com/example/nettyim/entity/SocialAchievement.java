package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社交成就实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("social_achievements")
public class SocialAchievement extends BaseEntity {
    
    /**
     * 成就ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 成就代码
     */
    private String achievementCode;
    
    /**
     * 成就名称
     */
    private String achievementName;
    
    /**
     * 成就描述
     */
    private String achievementDescription;
    
    /**
     * 成就图标
     */
    private String achievementIcon;
    
    /**
     * 成就分类
     */
    private String category;
    
    /**
     * 成就类型：1-一次性，2-进阶型，3-日常型
     */
    private Integer type;
    
    /**
     * 目标值
     */
    private Integer targetValue;
    
    /**
     * 积分奖励
     */
    private Integer pointsReward;
    
    /**
     * 经验奖励
     */
    private Integer expReward;
    
    /**
     * 徽章奖励
     */
    private String badgeReward;
    
    /**
     * 是否激活
     */
    private Boolean isActive;
    
    /**
     * 排序
     */
    private Integer sortOrder;
}