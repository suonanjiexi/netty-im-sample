package com.example.nettyim.service;

import com.example.nettyim.entity.SecurityLog;

import java.util.List;
import java.util.Map;

/**
 * 账号安全服务接口
 */
public interface SecurityService {
    
    /**
     * 记录安全日志
     */
    Boolean logSecurityEvent(Long userId, String actionType, String ipAddress, String userAgent, 
                           String location, Map<String, Object> deviceInfo, Boolean result, Integer riskLevel, String description);
    
    /**
     * 获取用户安全日志
     */
    List<Map<String, Object>> getUserSecurityLogs(Long userId, Integer limit);
    
    /**
     * 检查登录风险
     */
    Map<String, Object> assessLoginRisk(Long userId, String ipAddress, String userAgent, Map<String, Object> deviceInfo);
    
    /**
     * 检测异常登录
     */
    Boolean detectAbnormalLogin(Long userId, String ipAddress, String location);
    
    /**
     * 发送安全警告
     */
    Boolean sendSecurityAlert(Long userId, String alertType, Map<String, Object> alertData);
    
    /**
     * 启用两步验证
     */
    Boolean enableTwoFactorAuth(Long userId, String method);
    
    /**
     * 禁用两步验证
     */
    Boolean disableTwoFactorAuth(Long userId);
    
    /**
     * 验证两步验证码
     */
    Boolean verifyTwoFactorCode(Long userId, String code);
    
    /**
     * 生成备用验证码
     */
    List<String> generateBackupCodes(Long userId);
    
    /**
     * 强制下线所有设备
     */
    Boolean forceLogoutAllDevices(Long userId);
    
    /**
     * 冻结账号
     */
    Boolean freezeAccount(Long userId, String reason, Integer hours);
    
    /**
     * 解冻账号
     */
    Boolean unfreezeAccount(Long userId);
    
    /**
     * 获取安全状态概览
     */
    Map<String, Object> getSecurityOverview(Long userId);
    
    /**
     * 获取登录设备列表
     */
    List<Map<String, Object>> getLoginDevices(Long userId);
    
    /**
     * 移除登录设备
     */
    Boolean removeLoginDevice(Long userId, String deviceId);
}