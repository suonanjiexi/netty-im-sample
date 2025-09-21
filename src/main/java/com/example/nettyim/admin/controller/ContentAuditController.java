package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.service.ContentAuditManagementService;
import com.example.nettyim.entity.ContentAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 内容审核管理控制器
 */
@RestController
@RequestMapping("/admin/api/content-audits")
public class ContentAuditController {
    
    @Autowired
    private ContentAuditManagementService contentAuditManagementService;
    
    /**
     * 分页查询审核记录列表
     */
    @GetMapping
    public Map<String, Object> pageAudits(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer contentType,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) String keyword) {
        
        Page<ContentAudit> pageRequest = new Page<>(page, size);
        IPage<ContentAudit> pageResult = contentAuditManagementService.pageAudits(pageRequest, contentType, auditStatus, keyword);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取审核记录详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getAuditById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        ContentAudit audit = contentAuditManagementService.getById(id);
        if (audit != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", audit);
        } else {
            result.put("success", false);
            result.put("message", "审核记录不存在");
        }
        return result;
    }
    
    /**
     * 人工审核内容
     */
    @PostMapping("/{id}/manual-review")
    public Map<String, Object> manualReview(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        Integer auditStatus = (Integer) params.get("auditStatus");
        String auditResult = (String) params.get("auditResult");
        Long auditorId = Long.valueOf(params.get("auditorId").toString());
        
        boolean success = contentAuditManagementService.manualReview(id, auditStatus, auditResult, auditorId);
        if (success) {
            result.put("success", true);
            result.put("message", "审核成功");
        } else {
            result.put("success", false);
            result.put("message", "审核失败");
        }
        return result;
    }
    
    /**
     * 批量审核内容
     */
    @PostMapping("/batch-review")
    public Map<String, Object> batchReview(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        String auditIds = (String) params.get("auditIds");
        Integer auditStatus = (Integer) params.get("auditStatus");
        String auditResult = (String) params.get("auditResult");
        Long auditorId = Long.valueOf(params.get("auditorId").toString());
        
        boolean success = contentAuditManagementService.batchReview(auditIds, auditStatus, auditResult, auditorId);
        if (success) {
            result.put("success", true);
            result.put("message", "批量审核成功");
        } else {
            result.put("success", false);
            result.put("message", "批量审核失败");
        }
        return result;
    }
    
    /**
     * 获取审核统计数据
     */
    @GetMapping("/statistics")
    public Map<String, Object> getAuditStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> statistics = contentAuditManagementService.getAuditStatistics(startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", statistics);
        return result;
    }
    
    /**
     * 构造分页结果
     */
    private Map<String, Object> getPageResultMap(IPage<ContentAudit> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        return result;
    }
}