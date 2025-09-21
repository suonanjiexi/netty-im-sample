package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.entity.AdminOperationLog;
import com.example.nettyim.admin.service.AdminOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员操作日志控制器
 */
@RestController
@RequestMapping("/admin/api/logs")
public class AdminOperationLogController {
    
    @Autowired
    private AdminOperationLogService adminOperationLogService;
    
    /**
     * 分页查询操作日志列表
     */
    @GetMapping
    public Map<String, Object> pageOperationLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long adminUserId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operation) {
        
        Page<AdminOperationLog> pageRequest = new Page<>(page, size);
        IPage<AdminOperationLog> pageResult = adminOperationLogService.pageOperationLogs(
                pageRequest, adminUserId, module, operation);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 删除操作日志
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteOperationLog(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = adminOperationLogService.deleteOperationLog(id);
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
     * 构造分页结果
     */
    private Map<String, Object> getPageResultMap(IPage<AdminOperationLog> page) {
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