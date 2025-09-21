package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.service.SecurityLogService;
import com.example.nettyim.entity.SecurityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 安全日志控制器
 */
@RestController
@RequestMapping("/admin/api/security-logs")
public class SecurityLogController {
    
    @Autowired
    private SecurityLogService securityLogService;
    
    /**
     * 分页查询安全日志列表
     */
    @GetMapping
    public Map<String, Object> pageSecurityLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) Integer riskLevel,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Page<SecurityLog> pageRequest = new Page<>(page, size);
        IPage<SecurityLog> pageResult = securityLogService.pageSecurityLogs(
                pageRequest, userId, actionType, riskLevel, startTime, endTime);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取安全日志统计数据
     */
    @GetMapping("/statistics")
    public Map<String, Object> getSecurityLogStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> statistics = securityLogService.getSecurityLogStatistics(startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", statistics);
        return result;
    }
    
    /**
     * 根据ID获取安全日志
     */
    @GetMapping("/{id}")
    public Map<String, Object> getSecurityLogById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        SecurityLog log = securityLogService.getById(id);
        if (log != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", log);
        } else {
            result.put("success", false);
            result.put("message", "安全日志不存在");
        }
        return result;
    }
    
    /**
     * 删除安全日志
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteSecurityLog(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = securityLogService.deleteSecurityLog(id);
        if (success) {
            result.put("success", true);
            result.put("message", "删除成功");
        } else {
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }
    
    /**
     * 批量删除安全日志
     */
    @DeleteMapping("/batch")
    public Map<String, Object> batchDeleteSecurityLogs(@RequestParam String ids) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = securityLogService.batchDeleteSecurityLogs(ids);
        if (success) {
            result.put("success", true);
            result.put("message", "批量删除成功");
        } else {
            result.put("success", false);
            result.put("message", "批量删除失败");
        }
        return result;
    }
    
    /**
     * 构造分页结果
     */
    private Map<String, Object> getPageResultMap(IPage<SecurityLog> page) {
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