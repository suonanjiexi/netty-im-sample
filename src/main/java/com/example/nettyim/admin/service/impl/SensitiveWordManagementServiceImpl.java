package com.example.nettyim.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.admin.service.SensitiveWordManagementService;
import com.example.nettyim.entity.SensitiveWord;
import com.example.nettyim.mapper.SensitiveWordMapper;
import com.example.nettyim.utils.SensitiveWordFilter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词管理服务实现类
 */
@Service
public class SensitiveWordManagementServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordManagementService {
    
    private final SensitiveWordMapper sensitiveWordMapper;
    private final SensitiveWordFilter sensitiveWordFilter;
    
    public SensitiveWordManagementServiceImpl(SensitiveWordMapper sensitiveWordMapper, SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordMapper = sensitiveWordMapper;
        this.sensitiveWordFilter = sensitiveWordFilter;
    }
    
    @Override
    public IPage<SensitiveWord> pageSensitiveWords(Page<SensitiveWord> page, Integer category, Integer level, Integer status) {
        QueryWrapper<SensitiveWord> queryWrapper = new QueryWrapper<>();
        
        if (category != null) {
            queryWrapper.eq("category", category);
        }
        
        if (level != null) {
            queryWrapper.eq("level", level);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("created_at");
        return sensitiveWordMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public boolean addSensitiveWord(SensitiveWord sensitiveWord) {
        // 设置创建者为系统管理员（ID为0）
        sensitiveWord.setCreatorId(0L);
        return sensitiveWordMapper.insert(sensitiveWord) > 0;
    }
    
    @Override
    public boolean updateSensitiveWord(Long id, SensitiveWord sensitiveWord) {
        sensitiveWord.setId(id);
        return sensitiveWordMapper.updateById(sensitiveWord) > 0;
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setId(id);
        sensitiveWord.setStatus(status);
        return sensitiveWordMapper.updateById(sensitiveWord) > 0;
    }
    
    @Override
    public Map<String, Object> checkSensitiveWordsForAdmin(String text) {
        SensitiveWordFilter.SensitiveWordResult result = sensitiveWordFilter.checkSensitiveWords(text);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("hasSensitiveWords", result.hasSensitiveWords());
        resultMap.put("sensitiveWords", result.getSensitiveWords());
        resultMap.put("matches", result.getMatches());
        resultMap.put("shouldReject", result.shouldReject());
        resultMap.put("needsManualReview", result.needsManualReview());
        
        return resultMap;
    }
    
    @Override
    public String filterSensitiveWordsForAdmin(String text) {
        return sensitiveWordFilter.filterSensitiveWords(text);
    }
    
    // 实现SensitiveWordService接口中的方法（直接调用父类方法）
    
    @Override
    public IPage<SensitiveWord> getSensitiveWords(Page<SensitiveWord> page, Integer category, Integer level, Integer status) {
        return pageSensitiveWords(page, category, level, status);
    }
    
    @Override
    public Boolean addSensitiveWord(com.example.nettyim.dto.SensitiveWordDTO dto, Long creatorId) {
        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setWord(dto.getWord());
        sensitiveWord.setCategory(dto.getCategory());
        sensitiveWord.setLevel(dto.getLevel());
        sensitiveWord.setAction(dto.getAction());
        sensitiveWord.setReplacement(dto.getReplacement());
        sensitiveWord.setStatus(dto.getStatus());
        sensitiveWord.setCreatorId(creatorId);
        return sensitiveWordMapper.insert(sensitiveWord) > 0;
    }
    
    @Override
    public Boolean batchAddSensitiveWords(java.util.List<com.example.nettyim.dto.SensitiveWordDTO> dtos, Long creatorId) {
        boolean allSuccess = true;
        for (com.example.nettyim.dto.SensitiveWordDTO dto : dtos) {
            Boolean result = addSensitiveWord(dto, creatorId);
            if (result == null || !result) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }
    
    @Override
    public Boolean updateSensitiveWord(Long id, com.example.nettyim.dto.SensitiveWordDTO dto) {
        SensitiveWord sensitiveWord = sensitiveWordMapper.selectById(id);
        if (sensitiveWord == null) {
            return false;
        }
        
        sensitiveWord.setWord(dto.getWord());
        sensitiveWord.setCategory(dto.getCategory());
        sensitiveWord.setLevel(dto.getLevel());
        sensitiveWord.setAction(dto.getAction());
        sensitiveWord.setReplacement(dto.getReplacement());
        sensitiveWord.setStatus(dto.getStatus());
        
        return sensitiveWordMapper.updateById(sensitiveWord) > 0;
    }
    
    @Override
    public Boolean deleteSensitiveWord(Long id) {
        return sensitiveWordMapper.deleteById(id) > 0;
    }
    
    @Override
    public Boolean toggleSensitiveWord(Long id, Integer status) {
        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setId(id);
        sensitiveWord.setStatus(status);
        return sensitiveWordMapper.updateById(sensitiveWord) > 0;
    }
    
    @Override
    public SensitiveWord getSensitiveWordById(Long id) {
        return sensitiveWordMapper.selectById(id);
    }
    
    @Override
    public SensitiveWordFilter.SensitiveWordResult checkSensitiveWords(String text) {
        return sensitiveWordFilter.checkSensitiveWords(text);
    }
    
    @Override
    public String filterSensitiveWords(String text) {
        return sensitiveWordFilter.filterSensitiveWords(text);
    }
    
    @Override
    public void refreshSensitiveWordCache() {
        // 这里应该重新加载敏感词数据并重建AC自动机
        // 由于这是示例代码，我们简单地重新初始化
        // 在实际应用中，这里应该从数据库重新加载数据
    }
    
    @Override
    public java.util.List<SensitiveWord> getAllEnabledSensitiveWords() {
        QueryWrapper<SensitiveWord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return sensitiveWordMapper.selectList(queryWrapper);
    }
}