package com.example.nettyim.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.admin.service.ContentAuditManagementService;
import com.example.nettyim.entity.ContentAudit;
import com.example.nettyim.entity.enums.AuditStatus;
import com.example.nettyim.mapper.ContentAuditMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容审核管理服务实现类
 */
@Service
public class ContentAuditManagementServiceImpl extends ServiceImpl<ContentAuditMapper, ContentAudit> implements ContentAuditManagementService {
    
    private final ContentAuditMapper contentAuditMapper;
    
    public ContentAuditManagementServiceImpl(ContentAuditMapper contentAuditMapper) {
        this.contentAuditMapper = contentAuditMapper;
    }
    
    @Override
    public IPage<ContentAudit> pageAudits(Page<ContentAudit> page, Integer contentType, Integer auditStatus, String keyword) {
        QueryWrapper<ContentAudit> queryWrapper = new QueryWrapper<>();
        
        if (contentType != null) {
            queryWrapper.eq("content_type", contentType);
        }
        
        if (auditStatus != null) {
            queryWrapper.eq("audit_status", auditStatus);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("content", keyword);
        }
        
        queryWrapper.orderByDesc("created_at");
        return contentAuditMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public ContentAudit getById(Long id) {
        return contentAuditMapper.selectById(id);
    }
    
    @Override
    public boolean manualReview(Long id, Integer auditStatus, String auditResult, Long auditorId) {
        ContentAudit audit = contentAuditMapper.selectById(id);
        if (audit == null) {
            return false;
        }
        
        audit.setAuditStatus(auditStatus);
        audit.setAuditResult(auditResult);
        audit.setAuditorId(auditorId);
        audit.setAuditTime(LocalDateTime.now());
        audit.setNeedManualReview(0);
        
        return contentAuditMapper.updateById(audit) > 0;
    }
    
    @Override
    public boolean batchReview(String auditIds, Integer auditStatus, String auditResult, Long auditorId) {
        if (auditIds == null || auditIds.isEmpty()) {
            return false;
        }
        
        List<Long> ids = Arrays.stream(auditIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        
        boolean allSuccess = true;
        for (Long id : ids) {
            boolean success = manualReview(id, auditStatus, auditResult, auditorId);
            if (!success) {
                allSuccess = false;
            }
        }
        
        return allSuccess;
    }
    
    @Override
    public Map<String, Object> getAuditStatistics(String startTime, String endTime) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        if (startTime != null && !startTime.isEmpty()) {
            start = LocalDateTime.parse(startTime, formatter);
        }
        
        if (endTime != null && !endTime.isEmpty()) {
            end = LocalDateTime.parse(endTime, formatter);
        }
        
        List<Map<String, Object>> statistics = contentAuditMapper.selectAuditStatistics(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", 0L);
        result.put("pending", 0L);
        result.put("approved", 0L);
        result.put("rejected", 0L);
        result.put("autoApproved", 0L);
        result.put("autoRejected", 0L);
        
        Long total = 0L;
        for (Map<String, Object> stat : statistics) {
            Integer status = (Integer) stat.get("audit_status");
            Long count = ((Number) stat.get("count")).longValue();
            total += count;
            
            switch (status) {
                case 0 -> result.put("pending", count);
                case 1 -> result.put("approved", count);
                case 2 -> result.put("rejected", count);
                case 3 -> result.put("autoApproved", count);
                case 4 -> result.put("autoRejected", count);
            }
        }
        
        result.put("total", total);
        return result;
    }
    
    // 实现ContentAuditService接口中的方法（直接调用父类方法）
    
    @Override
    public com.example.nettyim.entity.ContentAudit autoAuditContent(com.example.nettyim.entity.enums.ContentType contentType, Long contentId, Long userId, String content) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Boolean submitForManualAudit(com.example.nettyim.entity.enums.ContentType contentType, Long contentId, Long userId, String content, String reason) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Boolean manualAudit(Long auditId, Long auditorId, com.example.nettyim.dto.AuditActionDTO auditAction) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<com.example.nettyim.dto.ContentAuditDTO> getAuditRecords(com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.example.nettyim.dto.ContentAuditDTO> page, Integer contentType, Integer auditStatus, Long userId, Long auditorId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Long getPendingAuditCount() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public java.util.List<com.example.nettyim.entity.ContentAudit> getUserAuditRecords(Long userId, Integer limit) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public com.example.nettyim.entity.ContentAudit getContentAuditRecord(com.example.nettyim.entity.enums.ContentType contentType, Long contentId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Boolean needsAudit(String content) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Map<String, Object> getAuditStatistics(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Boolean batchAudit(java.util.List<Long> auditIds, Long auditorId, com.example.nettyim.dto.AuditActionDTO auditAction) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public Boolean deleteAuditRecord(Long auditId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}