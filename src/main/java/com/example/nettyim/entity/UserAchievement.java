package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户成就记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_achievements")
public class UserAchievement extends BaseEntity {
    
    /**
     * 用户成就ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 成就ID
     */
    private Long achievementId;
    
    /**
     * 当前进度值
     */
    private Integer currentValue;
    
    /**
     * 目标值
     */
    private Integer targetValue;
    
    /**
     * 是否完成
     */
    private Boolean isCompleted;
    
    /**
     * 完成时间
     */
    private LocalDateTime completedAt;
    
    /**
     * 成就等级（进阶型成就）
     */
    private Integer level;
    
    /**
     * 获取进度百分比
     */
    public double getProgressPercentage() {
        if (targetValue == null || targetValue == 0) {
            return 0.0;
        }
        return Math.min(100.0, (currentValue != null ? currentValue : 0) * 100.0 / targetValue);
    }
}