package com.example.nettyim.service;

import com.example.nettyim.entity.SocialAchievement;
import com.example.nettyim.entity.UserAchievement;

import java.util.List;
import java.util.Map;

/**
 * 社交成就服务接口
 */
public interface SocialAchievementService {
    
    /**
     * 获取所有活跃成就
     */
    List<SocialAchievement> getAllActiveAchievements();
    
    /**
     * 获取用户成就列表
     */
    List<UserAchievement> getUserAchievements(Long userId);
    
    /**
     * 获取用户已完成的成就
     */
    List<UserAchievement> getUserCompletedAchievements(Long userId);
    
    /**
     * 获取用户进行中的成就
     */
    List<UserAchievement> getUserInProgressAchievements(Long userId);
    
    /**
     * 更新用户成就进度
     */
    Boolean updateAchievementProgress(Long userId, String achievementCode, Integer increment);
    
    /**
     * 检查并触发成就完成
     */
    List<UserAchievement> checkAndTriggerAchievements(Long userId, String actionType, Map<String, Object> actionData);
    
    /**
     * 初始化用户成就
     */
    Boolean initializeUserAchievements(Long userId);
    
    /**
     * 获取成就统计信息
     */
    Map<String, Object> getAchievementStats(Long userId);
    
    /**
     * 获取成就分类列表
     */
    List<String> getAchievementCategories();
    
    /**
     * 根据分类获取成就
     */
    List<SocialAchievement> getAchievementsByCategory(String category);
    
    /**
     * 处理成就完成奖励
     */
    Boolean processAchievementRewards(Long userId, Long achievementId);
}