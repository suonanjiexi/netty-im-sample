package com.example.nettyim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.dto.AuditActionDTO;
import com.example.nettyim.dto.ContentAuditDTO;
import com.example.nettyim.entity.ContentAudit;
import com.example.nettyim.entity.User;
import com.example.nettyim.entity.enums.AuditStatus;
import com.example.nettyim.entity.enums.ContentType;
import com.example.nettyim.mapper.ContentAuditMapper;
import com.example.nettyim.mapper.UserMapper;
import com.example.nettyim.service.ContentAuditService;
import com.example.nettyim.service.SensitiveWordService;
import com.example.nettyim.utils.SensitiveWordFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容审核服务实现
 */
@Slf4j
@Service
public class ContentAuditServiceImpl extends ServiceImpl<ContentAuditMapper, ContentAudit> implements ContentAuditService {
    
    @Autowired
    private ContentAuditMapper contentAuditMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private SensitiveWordService sensitiveWordService;
    
    // 审核阈值配置
    private static final int AUTO_PASS_THRESHOLD = 80;
    private static final int AUTO_REJECT_THRESHOLD = 30;
    
    @Override
    public ContentAudit autoAuditContent(ContentType contentType, Long contentId, Long userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            return null;
        }
        
        // 检查是否已存在审核记录
        ContentAudit existingAudit = getContentAuditRecord(contentType, contentId);
        if (existingAudit != null) {
            return existingAudit;
        }
        
        // 敏感词检测
        SensitiveWordFilter.SensitiveWordResult sensitiveResult = sensitiveWordService.checkSensitiveWords(content);
        
        ContentAudit audit = new ContentAudit();
        audit.setContentType(contentType.getCode());
        audit.setContentId(contentId);
        audit.setUserId(userId);
        audit.setContent(content);
        audit.setAuditType(0); // 自动审核
        audit.setCreatedAt(LocalDateTime.now());
        audit.setUpdatedAt(LocalDateTime.now());
        
        // 计算审核分数
        int auditScore = calculateAuditScore(content, sensitiveResult);
        audit.setAuditScore(auditScore);
        
        // 设置敏感词信息
        if (sensitiveResult.hasSensitiveWords()) {
            String sensitiveWords = String.join(",", sensitiveResult.getSensitiveWords());
            audit.setSensitiveWords(sensitiveWords);
        }
        
        // 根据敏感词检测结果和分数决定审核状态
        if (sensitiveResult.shouldReject() || auditScore < AUTO_REJECT_THRESHOLD) {
            audit.setAuditStatus(AuditStatus.AUTO_REJECTED.getCode());
            audit.setAuditResult("内容包含敏感词或不当内容，自动拒绝");
            audit.setNeedManualReview(0);
        } else if (sensitiveResult.needsManualReview() || 
                   (auditScore >= AUTO_REJECT_THRESHOLD && auditScore < AUTO_PASS_THRESHOLD)) {
            audit.setAuditStatus(AuditStatus.PENDING.getCode());
            audit.setAuditResult("内容需要人工审核");
            audit.setNeedManualReview(1);
        } else {
            audit.setAuditStatus(AuditStatus.AUTO_APPROVED.getCode());
            audit.setAuditResult("内容审核通过");
            audit.setNeedManualReview(0);
        }
        
        audit.setAuditTime(LocalDateTime.now());
        save(audit);
        
        log.info("自动审核完成 - 内容类型: {}, 内容ID: {}, 用户ID: {}, 审核状态: {}, 审核分数: {}", 
                contentType, contentId, userId, audit.getAuditStatus(), auditScore);
        
        return audit;
    }
    
    @Override
    public Boolean submitForManualAudit(ContentType contentType, Long contentId, Long userId, String content, String reason) {
        ContentAudit audit = new ContentAudit();
        audit.setContentType(contentType.getCode());
        audit.setContentId(contentId);
        audit.setUserId(userId);
        audit.setContent(content);
        audit.setAuditType(1); // 人工审核
        audit.setAuditStatus(AuditStatus.PENDING.getCode());
        audit.setAuditResult(reason != null ? reason : "提交人工审核");
        audit.setNeedManualReview(1);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setUpdatedAt(LocalDateTime.now());
        
        return save(audit);
    }
    
    @Override
    @Transactional
    public Boolean manualAudit(Long auditId, Long auditorId, AuditActionDTO auditAction) {
        ContentAudit audit = getById(auditId);
        if (audit == null) {
            log.warn("审核记录不存在: {}", auditId);
            return false;
        }
        
        if (!AuditStatus.PENDING.getCode().equals(audit.getAuditStatus())) {
            log.warn("审核记录状态不正确: {}, 当前状态: {}", auditId, audit.getAuditStatus());
            return false;
        }
        
        audit.setAuditStatus(auditAction.getAuditStatus());
        audit.setAuditResult(auditAction.getAuditResult());
        audit.setAuditorId(auditorId);
        audit.setAuditTime(LocalDateTime.now());
        audit.setUpdatedAt(LocalDateTime.now());
        audit.setNeedManualReview(0);
        
        boolean result = updateById(audit);
        if (result) {
            log.info("人工审核完成 - 审核ID: {}, 审核员ID: {}, 审核结果: {}", auditId, auditorId, auditAction.getAuditStatus());
        }
        
        return result;
    }
    
    @Override
    public IPage<ContentAuditDTO> getAuditRecords(Page<ContentAuditDTO> page, Integer contentType, Integer auditStatus,
                                                  Long userId, Long auditorId, LocalDateTime startTime, LocalDateTime endTime) {
        IPage<ContentAudit> auditPage = contentAuditMapper.selectAuditPage(
                new Page<>(page.getCurrent(), page.getSize()),
                contentType, auditStatus, userId, auditorId, startTime, endTime
        );
        
        // 转换为DTO并补充用户信息
        List<ContentAuditDTO> auditDTOs = auditPage.getRecords().stream().map(audit -> {
            ContentAuditDTO dto = new ContentAuditDTO();
            BeanUtils.copyProperties(audit, dto);
            
            // 获取用户昵称
            if (audit.getUserId() != null) {
                User user = userMapper.selectById(audit.getUserId());
                if (user != null) {
                    dto.setUserNickname(user.getNickname());
                }
            }
            
            // 获取审核员昵称
            if (audit.getAuditorId() != null) {
                User auditor = userMapper.selectById(audit.getAuditorId());
                if (auditor != null) {
                    dto.setAuditorNickname(auditor.getNickname());
                }
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        Page<ContentAuditDTO> resultPage = new Page<>(page.getCurrent(), page.getSize(), auditPage.getTotal());
        resultPage.setRecords(auditDTOs);
        
        return resultPage;
    }
    
    @Override
    public Long getPendingAuditCount() {
        return contentAuditMapper.countPendingAudits();
    }
    
    @Override
    public List<ContentAudit> getUserAuditRecords(Long userId, Integer limit) {
        return contentAuditMapper.selectByUserId(userId, limit != null ? limit : 10);
    }
    
    @Override
    public ContentAudit getContentAuditRecord(ContentType contentType, Long contentId) {
        return contentAuditMapper.selectByContentTypeAndId(contentType.getCode(), contentId);
    }
    
    @Override
    public Boolean needsAudit(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        // 检查敏感词
        SensitiveWordFilter.SensitiveWordResult result = sensitiveWordService.checkSensitiveWords(content);
        return result.hasSensitiveWords() || result.needsManualReview();
    }
    
    @Override
    public Map<String, Object> getAuditStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> statistics = contentAuditMapper.selectAuditStatistics(startTime, endTime);
        
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
    
    @Override
    @Transactional
    public Boolean batchAudit(List<Long> auditIds, Long auditorId, AuditActionDTO auditAction) {
        boolean allSuccess = true;
        for (Long auditId : auditIds) {
            boolean result = manualAudit(auditId, auditorId, auditAction);
            if (!result) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }
    
    @Override
    public Boolean deleteAuditRecord(Long auditId) {
        return removeById(auditId);
    }
    
    /**
     * 计算审核分数
     * 基础分数100，根据敏感词数量、等级等因素扣分
     */
    private int calculateAuditScore(String content, SensitiveWordFilter.SensitiveWordResult sensitiveResult) {
        int score = 100;
        
        if (sensitiveResult.hasSensitiveWords()) {
            for (SensitiveWordFilter.SensitiveWordMatch match : sensitiveResult.getMatches()) {
                // 根据敏感词等级扣分
                switch (match.getLevel()) {
                    case 1 -> score -= 5;  // 低级敏感词扣5分
                    case 2 -> score -= 15; // 中级敏感词扣15分
                    case 3 -> score -= 30; // 高级敏感词扣30分
                }
            }
        }
        
        // 内容长度因子（过短或过长的内容可能有问题）
        int contentLength = content.length();
        if (contentLength < 5) {
            score -= 10;
        } else if (contentLength > 5000) {
            score -= 5;
        }
        
        // 确保分数在0-100范围内
        return Math.max(0, Math.min(100, score));
    }
}