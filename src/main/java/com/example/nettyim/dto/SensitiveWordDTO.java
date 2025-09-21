package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 敏感词DTO
 */
@Data
public class SensitiveWordDTO {
    
    /**
     * 敏感词ID
     */
    private Long id;
    
    /**
     * 敏感词内容
     */
    @NotBlank(message = "敏感词内容不能为空")
    private String word;
    
    /**
     * 敏感词分类：1-政治敏感，2-色情低俗，3-暴力血腥，4-赌博诈骗，5-毒品违法，6-其他
     */
    @NotNull(message = "敏感词分类不能为空")
    private Integer category;
    
    /**
     * 敏感等级：1-低，2-中，3-高
     */
    @NotNull(message = "敏感等级不能为空")
    private Integer level;
    
    /**
     * 处理方式：1-替换，2-拒绝，3-人工审核
     */
    @NotNull(message = "处理方式不能为空")
    private Integer action;
    
    /**
     * 替换词
     */
    private String replacement;
    
    /**
     * 是否启用：0-禁用，1-启用
     */
    private Integer status;
}