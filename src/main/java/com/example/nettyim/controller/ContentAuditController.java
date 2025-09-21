package com.example.nettyim.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.dto.AuditActionDTO;
import com.example.nettyim.dto.ContentAuditDTO;
import com.example.nettyim.dto.Result;
import com.example.nettyim.entity.ContentAudit;
import com.example.nettyim.service.ContentAuditService;
import com.example.nettyim.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 内容审核管理控制器
 */
@RestController
@RequestMapping("/admin/audit")
public class ContentAuditController {
    
    @Autowired
    private ContentAuditService contentAuditService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 分页查询审核记录
     */
    @GetMapping("/records")
    public Result<IPage<ContentAuditDTO>> getAuditRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Integer contentType,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long auditorId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        
        Page<ContentAuditDTO> pageParam = new Page<>(page, size);
        IPage<ContentAuditDTO> result = contentAuditService.getAuditRecords(
                pageParam, contentType, auditStatus, userId, auditorId, startTime, endTime);
        
        return Result.success(result);
    }
    
    /**
     * 人工审核
     */
    @PostMapping("/manual/{auditId}")
    public Result<Boolean> manualAudit(@PathVariable Long auditId,
                                       @Valid @RequestBody AuditActionDTO auditAction,
                                       HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long auditorId = jwtUtils.getUserIdFromToken(token);
        if (auditorId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean result = contentAuditService.manualAudit(auditId, auditorId, auditAction);
        return result ? Result.success(true) : Result.error("审核失败");
    }
    
    /**
     * 批量审核
     */
    @PostMapping("/batch")
    public Result<Boolean> batchAudit(@RequestParam List<Long> auditIds,
                                      @Valid @RequestBody AuditActionDTO auditAction,
                                      HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long auditorId = jwtUtils.getUserIdFromToken(token);
        if (auditorId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean result = contentAuditService.batchAudit(auditIds, auditorId, auditAction);
        return result ? Result.success(true) : Result.error("批量审核失败");
    }
    
    /**
     * 获取待审核记录数量
     */
    @GetMapping("/pending/count")
    public Result<Long> getPendingAuditCount() {
        Long count = contentAuditService.getPendingAuditCount();
        return Result.success(count);
    }
    
    /**
     * 获取用户的审核记录
     */
    @GetMapping("/user/{userId}")
    public Result<List<ContentAudit>> getUserAuditRecords(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "10") Integer limit) {
        List<ContentAudit> records = contentAuditService.getUserAuditRecords(userId, limit);
        return Result.success(records);
    }
    
    /**
     * 获取审核统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getAuditStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(7); // 默认查询最近7天
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        
        Map<String, Object> statistics = contentAuditService.getAuditStatistics(startTime, endTime);
        return Result.success(statistics);
    }
    
    /**
     * 删除审核记录
     */
    @DeleteMapping("/{auditId}")
    public Result<Boolean> deleteAuditRecord(@PathVariable Long auditId) {
        Boolean result = contentAuditService.deleteAuditRecord(auditId);
        return result ? Result.success(true) : Result.error("删除失败");
    }
}