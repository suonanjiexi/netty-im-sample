package com.example.nettyim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.dto.AuditActionDTO;
import com.example.nettyim.dto.ContentAuditDTO;
import com.example.nettyim.entity.ContentAudit;
import com.example.nettyim.entity.enums.ContentType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 内容审核服务接口
 */
public interface ContentAuditService {
    
    /**
     * 自动审核内容
     */
    ContentAudit autoAuditContent(ContentType contentType, Long contentId, Long userId, String content);
    
    /**
     * 提交人工审核
     */
    Boolean submitForManualAudit(ContentType contentType, Long contentId, Long userId, String content, String reason);
    
    /**
     * 人工审核操作
     */
    Boolean manualAudit(Long auditId, Long auditorId, AuditActionDTO auditAction);
    
    /**
     * 分页查询审核记录
     */
    IPage<ContentAuditDTO> getAuditRecords(Page<ContentAuditDTO> page, Integer contentType, Integer auditStatus, 
                                           Long userId, Long auditorId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取待审核记录数量
     */
    Long getPendingAuditCount();
    
    /**
     * 获取用户的审核记录
     */
    List<ContentAudit> getUserAuditRecords(Long userId, Integer limit);
    
    /**
     * 获取指定内容的审核记录
     */
    ContentAudit getContentAuditRecord(ContentType contentType, Long contentId);
    
    /**
     * 检查内容是否需要审核
     */
    Boolean needsAudit(String content);
    
    /**
     * 获取审核统计数据
     */
    Map<String, Object> getAuditStatistics(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 批量审核
     */
    Boolean batchAudit(List<Long> auditIds, Long auditorId, AuditActionDTO auditAction);
    
    /**
     * 删除审核记录
     */
    Boolean deleteAuditRecord(Long auditId);
}