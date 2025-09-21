package com.example.nettyim.service;

import com.example.nettyim.entity.PrivacySetting;

import java.util.List;
import java.util.Map;

/**
 * 隐私设置服务接口
 */
public interface PrivacyService {
    
    /**
     * 获取用户隐私设置
     */
    List<PrivacySetting> getUserPrivacySettings(Long userId);
    
    /**
     * 获取单个隐私设置
     */
    String getPrivacySetting(Long userId, String settingKey);
    
    /**
     * 更新隐私设置
     */
    Boolean updatePrivacySetting(Long userId, String settingKey, String settingValue);
    
    /**
     * 批量更新隐私设置
     */
    Boolean updatePrivacySettings(Long userId, Map<String, String> settings);
    
    /**
     * 初始化用户默认隐私设置
     */
    Boolean initializeUserPrivacySettings(Long userId);
    
    /**
     * 检查用户隐私权限
     */
    Boolean checkPrivacyPermission(Long userId, Long targetUserId, String permission);
    
    /**
     * 获取默认隐私设置
     */
    Map<String, String> getDefaultPrivacySettings();
    
    /**
     * 获取隐私设置分类
     */
    Map<String, List<Map<String, Object>>> getPrivacySettingCategories();
    
    /**
     * 重置隐私设置为默认值
     */
    Boolean resetPrivacySettings(Long userId);
    
    /**
     * 导出用户隐私设置
     */
    Map<String, Object> exportPrivacySettings(Long userId);
}