package com.example.nettyim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.dto.SensitiveWordDTO;
import com.example.nettyim.entity.SensitiveWord;
import com.example.nettyim.mapper.SensitiveWordMapper;
import com.example.nettyim.service.SensitiveWordService;
import com.example.nettyim.utils.SensitiveWordFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 敏感词服务实现
 */
@Slf4j
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {
    
    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private SensitiveWordFilter sensitiveWordFilter;
    
    private static final String SENSITIVE_WORD_CACHE_KEY = "sensitive:words:cache";
    
    @PostConstruct
    public void init() {
        this.sensitiveWordFilter = new SensitiveWordFilter();
        refreshSensitiveWordCache();
    }
    
    @Override
    public IPage<SensitiveWord> getSensitiveWords(Page<SensitiveWord> page, Integer category, Integer level, Integer status) {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category != null, SensitiveWord::getCategory, category)
                .eq(level != null, SensitiveWord::getLevel, level)
                .eq(status != null, SensitiveWord::getStatus, status)
                .orderByDesc(SensitiveWord::getCreatedAt);
        
        return page(page, wrapper);
    }
    
    @Override
    public Boolean addSensitiveWord(SensitiveWordDTO dto, Long creatorId) {
        // 检查敏感词是否已存在
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SensitiveWord::getWord, dto.getWord());
        SensitiveWord existing = getOne(wrapper);
        if (existing != null) {
            log.warn("敏感词已存在: {}", dto.getWord());
            return false;
        }
        
        SensitiveWord sensitiveWord = new SensitiveWord();
        BeanUtils.copyProperties(dto, sensitiveWord);
        sensitiveWord.setCreatorId(creatorId);
        sensitiveWord.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        sensitiveWord.setCreatedAt(LocalDateTime.now());
        sensitiveWord.setUpdatedAt(LocalDateTime.now());
        
        boolean result = save(sensitiveWord);
        if (result) {
            refreshSensitiveWordCache();
        }
        return result;
    }
    
    @Override
    public Boolean batchAddSensitiveWords(List<SensitiveWordDTO> dtos, Long creatorId) {
        List<SensitiveWord> sensitiveWords = dtos.stream().map(dto -> {
            SensitiveWord sensitiveWord = new SensitiveWord();
            BeanUtils.copyProperties(dto, sensitiveWord);
            sensitiveWord.setCreatorId(creatorId);
            sensitiveWord.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
            sensitiveWord.setCreatedAt(LocalDateTime.now());
            sensitiveWord.setUpdatedAt(LocalDateTime.now());
            return sensitiveWord;
        }).collect(Collectors.toList());
        
        boolean result = saveBatch(sensitiveWords);
        if (result) {
            refreshSensitiveWordCache();
        }
        return result;
    }
    
    @Override
    public Boolean updateSensitiveWord(Long id, SensitiveWordDTO dto) {
        SensitiveWord sensitiveWord = getById(id);
        if (sensitiveWord == null) {
            return false;
        }
        
        BeanUtils.copyProperties(dto, sensitiveWord);
        sensitiveWord.setUpdatedAt(LocalDateTime.now());
        
        boolean result = updateById(sensitiveWord);
        if (result) {
            refreshSensitiveWordCache();
        }
        return result;
    }
    
    @Override
    public Boolean deleteSensitiveWord(Long id) {
        boolean result = removeById(id);
        if (result) {
            refreshSensitiveWordCache();
        }
        return result;
    }
    
    @Override
    public Boolean toggleSensitiveWord(Long id, Integer status) {
        SensitiveWord sensitiveWord = getById(id);
        if (sensitiveWord == null) {
            return false;
        }
        
        sensitiveWord.setStatus(status);
        sensitiveWord.setUpdatedAt(LocalDateTime.now());
        
        boolean result = updateById(sensitiveWord);
        if (result) {
            refreshSensitiveWordCache();
        }
        return result;
    }
    
    @Override
    public SensitiveWord getSensitiveWordById(Long id) {
        return getById(id);
    }
    
    @Override
    public SensitiveWordFilter.SensitiveWordResult checkSensitiveWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new SensitiveWordFilter.SensitiveWordResult(List.of());
        }
        return sensitiveWordFilter.checkSensitiveWords(text);
    }
    
    @Override
    public String filterSensitiveWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        return sensitiveWordFilter.filterSensitiveWords(text);
    }
    
    @Override
    public void refreshSensitiveWordCache() {
        try {
            List<SensitiveWord> sensitiveWords = getAllEnabledSensitiveWords();
            
            // 转换为过滤器需要的格式
            List<SensitiveWordFilter.SensitiveWordInfo> wordInfos = sensitiveWords.stream()
                    .map(word -> new SensitiveWordFilter.SensitiveWordInfo(
                            word.getWord(),
                            word.getCategory(),
                            word.getLevel(),
                            word.getAction(),
                            word.getReplacement()
                    ))
                    .collect(Collectors.toList());
            
            // 重新构建AC自动机
            sensitiveWordFilter.buildAcAutomaton(wordInfos);
            
            // 缓存到Redis
            redisTemplate.opsForValue().set(SENSITIVE_WORD_CACHE_KEY, sensitiveWords, 1, TimeUnit.HOURS);
            
            log.info("敏感词缓存刷新成功，共加载{}个敏感词", sensitiveWords.size());
        } catch (Exception e) {
            log.error("刷新敏感词缓存失败", e);
        }
    }
    
    @Override
    public List<SensitiveWord> getAllEnabledSensitiveWords() {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SensitiveWord::getStatus, 1)
                .orderByDesc(SensitiveWord::getLevel)
                .orderByAsc(SensitiveWord::getWord);
        
        return list(wrapper);
    }
}