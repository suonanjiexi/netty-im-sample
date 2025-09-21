package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.ContentAudit;
import com.example.nettyim.service.ContentAuditService;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 内容审核管理服务接口（扩展）
 */
public interface ContentAuditManagementService extends ContentAuditService {
    
    /**
     * 分页查询审核记录列表
     * @param page 分页参数
     * @param contentType 内容类型
     * @param auditStatus 审核状态
     * @param keyword 关键词
     * @return 审核记录分页列表
     */
    IPage<ContentAudit> pageAudits(Page<ContentAudit> page, Integer contentType, Integer auditStatus, String keyword);
    
    /**
     * 根据ID获取审核记录
     * @param id 审核记录ID
     * @return 审核记录
     */
    ContentAudit getById(Long id);
    
    /**
     * 人工审核内容
     * @param id 审核记录ID
     * @param auditStatus 审核状态
     * @param auditResult 审核结果
     * @param auditorId 审核员ID
     * @return 是否成功
     */
    boolean manualReview(Long id, Integer auditStatus, String auditResult, Long auditorId);
    
    /**
     * 批量审核内容
     * @param auditIds 审核记录ID列表
     * @param auditStatus 审核状态
     * @param auditResult 审核结果
     * @param auditorId 审核员ID
     * @return 是否成功
     */
    boolean batchReview(String auditIds, Integer auditStatus, String auditResult, Long auditorId);
    
    /**
     * 获取审核统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    Map<String, Object> getAuditStatistics(String startTime, String endTime);
}