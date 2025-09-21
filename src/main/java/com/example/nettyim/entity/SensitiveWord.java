package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 敏感词实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sensitive_words")
public class SensitiveWord extends BaseEntity {
    
    /**
     * 敏感词ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 敏感词内容
     */
    private String word;
    
    /**
     * 敏感词分类：1-政治敏感，2-色情低俗，3-暴力血腥，4-赌博诈骗，5-毒品违法，6-其他
     */
    private Integer category;
    
    /**
     * 敏感等级：1-低，2-中，3-高
     */
    private Integer level;
    
    /**
     * 处理方式：1-替换，2-拒绝，3-人工审核
     */
    private Integer action;
    
    /**
     * 替换词（当action=1时使用）
     */
    private String replacement;
    
    /**
     * 是否启用：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getWord() {
        return word;
    }
    
    public void setWord(String word) {
        this.word = word;
    }
    
    public Integer getCategory() {
        return category;
    }
    
    public void setCategory(Integer category) {
        this.category = category;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getAction() {
        return action;
    }
    
    public void setAction(Integer action) {
        this.action = action;
    }
    
    public String getReplacement() {
        return replacement;
    }
    
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Long getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}