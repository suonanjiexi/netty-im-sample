package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.SensitiveWord;
import com.example.nettyim.service.SensitiveWordService;
import com.example.nettyim.utils.SensitiveWordFilter;

import java.util.Map;

/**
 * 敏感词管理服务接口（扩展）
 */
public interface SensitiveWordManagementService extends SensitiveWordService {
    
    /**
     * 分页查询敏感词列表
     * @param page 分页参数
     * @param category 分类
     * @param level 等级
     * @param status 状态
     * @return 敏感词分页列表
     */
    IPage<SensitiveWord> pageSensitiveWords(Page<SensitiveWord> page, Integer category, Integer level, Integer status);
    
    /**
     * 添加敏感词
     * @param sensitiveWord 敏感词信息
     * @return 是否成功
     */
    boolean addSensitiveWord(SensitiveWord sensitiveWord);
    
    /**
     * 更新敏感词
     * @param id 敏感词ID
     * @param sensitiveWord 敏感词信息
     * @return 是否成功
     */
    boolean updateSensitiveWord(Long id, SensitiveWord sensitiveWord);
    
    /**
     * 更新敏感词状态
     * @param id 敏感词ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 检测敏感词（运营管理专用版本，返回Map格式）
     * @param text 文本内容
     * @return 检测结果
     */
    Map<String, Object> checkSensitiveWordsForAdmin(String text);
    
    /**
     * 过滤敏感词（运营管理专用版本）
     * @param text 文本内容
     * @return 过滤后的文本
     */
    String filterSensitiveWordsForAdmin(String text);
}