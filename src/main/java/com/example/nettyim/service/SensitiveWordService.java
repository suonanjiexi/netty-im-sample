package com.example.nettyim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.dto.SensitiveWordDTO;
import com.example.nettyim.entity.SensitiveWord;
import com.example.nettyim.utils.SensitiveWordFilter;

import java.util.List;

/**
 * 敏感词服务接口
 */
public interface SensitiveWordService {
    
    /**
     * 分页查询敏感词
     */
    IPage<SensitiveWord> getSensitiveWords(Page<SensitiveWord> page, Integer category, Integer level, Integer status);
    
    /**
     * 添加敏感词
     */
    Boolean addSensitiveWord(SensitiveWordDTO dto, Long creatorId);
    
    /**
     * 批量添加敏感词
     */
    Boolean batchAddSensitiveWords(List<SensitiveWordDTO> dtos, Long creatorId);
    
    /**
     * 更新敏感词
     */
    Boolean updateSensitiveWord(Long id, SensitiveWordDTO dto);
    
    /**
     * 删除敏感词
     */
    Boolean deleteSensitiveWord(Long id);
    
    /**
     * 启用/禁用敏感词
     */
    Boolean toggleSensitiveWord(Long id, Integer status);
    
    /**
     * 获取敏感词详情
     */
    SensitiveWord getSensitiveWordById(Long id);
    
    /**
     * 检测文本中的敏感词
     */
    SensitiveWordFilter.SensitiveWordResult checkSensitiveWords(String text);
    
    /**
     * 过滤敏感词
     */
    String filterSensitiveWords(String text);
    
    /**
     * 刷新敏感词缓存
     */
    void refreshSensitiveWordCache();
    
    /**
     * 获取所有启用的敏感词
     */
    List<SensitiveWord> getAllEnabledSensitiveWords();
}