package com.example.nettyim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.HarassmentReport;

import java.util.List;
import java.util.Map;

/**
 * 防骚扰服务接口
 */
public interface AntiHarassmentService {
    
    /**
     * 提交骚扰举报
     */
    Boolean submitHarassmentReport(Long reporterId, Long reportedUserId, Integer reportType, 
                                  Integer contentType, Long contentId, String description, List<String> evidenceUrls);
    
    /**
     * 获取用户举报历史
     */
    IPage<HarassmentReport> getUserReports(Long userId, Page<HarassmentReport> page);
    
    /**
     * 拉黑用户
     */
    Boolean blockUser(Long userId, Long blockedUserId, String reason);
    
    /**
     * 取消拉黑用户
     */
    Boolean unblockUser(Long userId, Long blockedUserId);
    
    /**
     * 获取黑名单列表
     */
    List<Map<String, Object>> getBlockedUsers(Long userId);
    
    /**
     * 检查用户是否被拉黑
     */
    Boolean isUserBlocked(Long userId, Long targetUserId);
    
    /**
     * 设置消息过滤规则
     */
    Boolean setMessageFilterRules(Long userId, Map<String, Object> rules);
    
    /**
     * 检查消息是否需要过滤
     */
    Boolean shouldFilterMessage(Long userId, String message, Long fromUserId);
    
    /**
     * 自动检测骚扰行为
     */
    Boolean detectHarassmentBehavior(Long userId, Long targetUserId, String action, Map<String, Object> context);
    
    /**
     * 获取防骚扰统计
     */
    Map<String, Object> getAntiHarassmentStats(Long userId);
    
    /**
     * 处理举报（管理员）
     */
    Boolean processReport(Long reportId, Long adminId, Integer status, String adminNote);
    
    /**
     * 获取待处理举报列表（管理员）
     */
    IPage<HarassmentReport> getPendingReports(Page<HarassmentReport> page);
}