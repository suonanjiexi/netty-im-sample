package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.service.SensitiveWordManagementService;
import com.example.nettyim.entity.SensitiveWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词管理控制器
 */
@RestController
@RequestMapping("/admin/api/sensitive-words")
public class SensitiveWordController {
    
    @Autowired
    private SensitiveWordManagementService sensitiveWordManagementService;
    
    /**
     * 分页查询敏感词列表
     */
    @GetMapping
    public Map<String, Object> pageSensitiveWords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer status) {
        
        Page<SensitiveWord> pageRequest = new Page<>(page, size);
        IPage<SensitiveWord> pageResult = sensitiveWordManagementService.pageSensitiveWords(pageRequest, category, level, status);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 添加敏感词
     */
    @PostMapping
    public Map<String, Object> addSensitiveWord(@RequestBody SensitiveWord sensitiveWord) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = sensitiveWordManagementService.addSensitiveWord(sensitiveWord);
        if (success) {
            result.put("success", true);
            result.put("message", "添加成功");
        } else {
            result.put("success", false);
            result.put("message", "添加失败");
        }
        return result;
    }
    
    /**
     * 批量添加敏感词
     */
    @PostMapping("/batch")
    public Map<String, Object> batchAddSensitiveWords(@RequestBody List<SensitiveWord> sensitiveWords) {
        Map<String, Object> result = new HashMap<>();
        
        int successCount = 0;
        for (SensitiveWord sensitiveWord : sensitiveWords) {
            boolean success = sensitiveWordManagementService.addSensitiveWord(sensitiveWord);
            if (success) {
                successCount++;
            }
        }
        
        result.put("success", true);
        result.put("message", "批量添加完成，成功" + successCount + "条，失败" + (sensitiveWords.size() - successCount) + "条");
        result.put("data", Map.of("successCount", successCount, "totalCount", sensitiveWords.size()));
        return result;
    }
    
    /**
     * 更新敏感词
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateSensitiveWord(@PathVariable Long id, @RequestBody SensitiveWord sensitiveWord) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = sensitiveWordManagementService.updateSensitiveWord(id, sensitiveWord);
        if (success) {
            result.put("success", true);
            result.put("message", "更新成功");
        } else {
            result.put("success", false);
            result.put("message", "更新失败");
        }
        return result;
    }
    
    /**
     * 删除敏感词
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteSensitiveWord(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = sensitiveWordManagementService.deleteSensitiveWord(id);
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
     * 批量删除敏感词
     */
    @DeleteMapping("/batch")
    public Map<String, Object> batchDeleteSensitiveWords(@RequestParam String ids) {
        Map<String, Object> result = new HashMap<>();
        
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::valueOf)
                .toList();
        
        int successCount = 0;
        for (Long id : idList) {
            boolean success = sensitiveWordManagementService.deleteSensitiveWord(id);
            if (success) {
                successCount++;
            }
        }
        
        result.put("success", true);
        result.put("message", "批量删除完成，成功" + successCount + "条，失败" + (idList.size() - successCount) + "条");
        result.put("data", Map.of("successCount", successCount, "totalCount", idList.size()));
        return result;
    }
    
    /**
     * 更新敏感词状态
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = sensitiveWordManagementService.updateStatus(id, status);
        if (success) {
            result.put("success", true);
            result.put("message", "状态更新成功");
        } else {
            result.put("success", false);
            result.put("message", "状态更新失败");
        }
        return result;
    }
    
    /**
     * 检测敏感词
     */
    @PostMapping("/check")
    public Map<String, Object> checkSensitiveWords(@RequestParam String text) {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> checkResult = sensitiveWordManagementService.checkSensitiveWordsForAdmin(text);
        result.put("success", true);
        result.put("message", "检测完成");
        result.put("data", checkResult);
        return result;
    }
    
    /**
     * 过滤敏感词
     */
    @PostMapping("/filter")
    public Map<String, Object> filterSensitiveWords(@RequestParam String text) {
        Map<String, Object> result = new HashMap<>();
        
        String filteredText = sensitiveWordManagementService.filterSensitiveWordsForAdmin(text);
        result.put("success", true);
        result.put("message", "过滤完成");
        result.put("data", Map.of("filteredText", filteredText));
        return result;
    }
    
    /**
     * 构造分页结果
     */
    private Map<String, Object> getPageResultMap(IPage<SensitiveWord> page) {
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