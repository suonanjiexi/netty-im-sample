package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户经验记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_experience")
public class UserExperience extends BaseEntity {
    
    /**
     * 经验ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 当前等级
     */
    private Integer currentLevel;
    
    /**
     * 当前经验值
     */
    private Integer currentExp;
    
    /**
     * 累计经验值
     */
    private Integer totalExp;
    
    /**
     * 上次升级时间
     */
    private LocalDateTime lastLevelUpTime;
    
    /**
     * 计算到下一级需要的经验值
     */
    public Integer getExpToNextLevel(UserLevel nextLevel) {
        if (nextLevel == null || nextLevel.getMinExp() == null) {
            return null;
        }
        
        return Math.max(0, nextLevel.getMinExp() - (currentExp != null ? currentExp : 0));
    }
    
    /**
     * 计算当前等级的经验进度百分比
     */
    public double getLevelProgress(UserLevel currentLevelInfo, UserLevel nextLevelInfo) {
        if (currentLevelInfo == null || currentExp == null) {
            return 0.0;
        }
        
        int levelMinExp = currentLevelInfo.getMinExp() != null ? currentLevelInfo.getMinExp() : 0;
        int expInCurrentLevel = currentExp - levelMinExp;
        
        if (nextLevelInfo == null || nextLevelInfo.getMinExp() == null) {
            return 100.0;
        }
        
        int expRangeInCurrentLevel = nextLevelInfo.getMinExp() - levelMinExp;
        if (expRangeInCurrentLevel <= 0) {
            return 100.0;
        }
        
        return Math.min(100.0, expInCurrentLevel * 100.0 / expRangeInCurrentLevel);
    }
}