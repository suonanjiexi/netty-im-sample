package com.example.nettyim.service;

import com.example.nettyim.entity.UserLevel;
import com.example.nettyim.entity.UserExperience;

import java.util.List;
import java.util.Map;

/**
 * 用户等级经验服务接口
 */
public interface UserLevelService {
    
    /**
     * 获取所有等级配置
     */
    List<UserLevel> getAllLevels();
    
    /**
     * 根据经验值获取等级
     */
    UserLevel getLevelByExp(Integer exp);
    
    /**
     * 获取用户经验信息
     */
    UserExperience getUserExperience(Long userId);
    
    /**
     * 增加用户经验
     */
    Boolean addUserExperience(Long userId, Integer exp, String actionType, String description);
    
    /**
     * 检查用户是否升级
     */
    Boolean checkAndProcessLevelUp(Long userId);
    
    /**
     * 初始化用户经验
     */
    Boolean initializeUserExperience(Long userId);
    
    /**
     * 获取经验获得规则
     */
    Map<String, Integer> getExpRules();
    
    /**
     * 根据行为类型获取经验值
     */
    Integer getExpByActionType(String actionType);
    
    /**
     * 获取用户等级特权
     */
    List<String> getUserLevelPrivileges(Long userId);
    
    /**
     * 获取等级统计信息
     */
    Map<String, Object> getLevelStats(Long userId);
    
    /**
     * 获取经验排行榜
     */
    List<Map<String, Object>> getExpRanking(Integer limit);
}