package com.example.nettyim.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.dto.Result;
import com.example.nettyim.dto.SensitiveWordDTO;
import com.example.nettyim.entity.SensitiveWord;
import com.example.nettyim.service.SensitiveWordService;
import com.example.nettyim.utils.JwtUtils;
import com.example.nettyim.utils.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 敏感词管理控制器
 */
@RestController
@RequestMapping("/admin/sensitive-words")
public class SensitiveWordController {
    
    @Autowired
    private SensitiveWordService sensitiveWordService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 分页查询敏感词
     */
    @GetMapping
    public Result<IPage<SensitiveWord>> getSensitiveWords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer status) {
        
        Page<SensitiveWord> pageParam = new Page<>(page, size);
        IPage<SensitiveWord> result = sensitiveWordService.getSensitiveWords(pageParam, category, level, status);
        
        return Result.success(result);
    }
    
    /**
     * 添加敏感词
     */
    @PostMapping
    public Result<Boolean> addSensitiveWord(@Valid @RequestBody SensitiveWordDTO dto,
                                            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long creatorId = jwtUtils.getUserIdFromToken(token);
        if (creatorId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean result = sensitiveWordService.addSensitiveWord(dto, creatorId);
        return result ? Result.success(true) : Result.error("敏感词已存在");
    }
    
    /**
     * 批量添加敏感词
     */
    @PostMapping("/batch")
    public Result<Boolean> batchAddSensitiveWords(@Valid @RequestBody List<SensitiveWordDTO> dtos,
                                                  HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        Long creatorId = jwtUtils.getUserIdFromToken(token);
        if (creatorId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean result = sensitiveWordService.batchAddSensitiveWords(dtos, creatorId);
        return result ? Result.success(true) : Result.error("批量添加失败");
    }
    
    /**
     * 更新敏感词
     */
    @PutMapping("/{id}")
    public Result<Boolean> updateSensitiveWord(@PathVariable Long id,
                                               @Valid @RequestBody SensitiveWordDTO dto) {
        Boolean result = sensitiveWordService.updateSensitiveWord(id, dto);
        return result ? Result.success(true) : Result.error("更新失败");
    }
    
    /**
     * 删除敏感词
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSensitiveWord(@PathVariable Long id) {
        Boolean result = sensitiveWordService.deleteSensitiveWord(id);
        return result ? Result.success(true) : Result.error("删除失败");
    }
    
    /**
     * 启用/禁用敏感词
     */
    @PatchMapping("/{id}/status")
    public Result<Boolean> toggleSensitiveWord(@PathVariable Long id,
                                               @RequestParam Integer status) {
        Boolean result = sensitiveWordService.toggleSensitiveWord(id, status);
        return result ? Result.success(true) : Result.error("状态更新失败");
    }
    
    /**
     * 获取敏感词详情
     */
    @GetMapping("/{id}")
    public Result<SensitiveWord> getSensitiveWordById(@PathVariable Long id) {
        SensitiveWord sensitiveWord = sensitiveWordService.getSensitiveWordById(id);
        return sensitiveWord != null ? Result.success(sensitiveWord) : Result.error("敏感词不存在");
    }
    
    /**
     * 检测文本中的敏感词
     */
    @PostMapping("/check")
    public Result<SensitiveWordFilter.SensitiveWordResult> checkSensitiveWords(@RequestParam String text) {
        SensitiveWordFilter.SensitiveWordResult result = sensitiveWordService.checkSensitiveWords(text);
        return Result.success(result);
    }
    
    /**
     * 过滤敏感词
     */
    @PostMapping("/filter")
    public Result<String> filterSensitiveWords(@RequestParam String text) {
        String filteredText = sensitiveWordService.filterSensitiveWords(text);
        return Result.success(filteredText);
    }
    
    /**
     * 刷新敏感词缓存
     */
    @PostMapping("/refresh")
    public Result<String> refreshSensitiveWordCache() {
        sensitiveWordService.refreshSensitiveWordCache();
        return Result.success("缓存刷新成功");
    }
    
    /**
     * 获取所有启用的敏感词
     */
    @GetMapping("/enabled")
    public Result<List<SensitiveWord>> getAllEnabledSensitiveWords() {
        List<SensitiveWord> sensitiveWords = sensitiveWordService.getAllEnabledSensitiveWords();
        return Result.success(sensitiveWords);
    }
}