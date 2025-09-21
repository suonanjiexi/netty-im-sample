package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 内容审核记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("content_audits")
public class ContentAudit extends BaseEntity {
    
    /**
     * 审核记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 内容类型：1-消息，2-朋友圈动态，3-朋友圈评论，4-贴吧帖子，5-贴吧回复，6-用户资料
     */
    private Integer contentType;
    
    /**
     * 内容ID
     */
    private Long contentId;
    
    /**
     * 内容发布者ID
     */
    private Long userId;
    
    /**
     * 审核内容（原始内容）
     */
    private String content;
    
    /**
     * 审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝
     */
    private Integer auditStatus;
    
    /**
     * 审核结果：审核通过/拒绝的原因
     */
    private String auditResult;
    
    /**
     * 审核员ID（人工审核）
     */
    private Long auditorId;
    
    /**
     * 审核方式：0-自动审核，1-人工审核
     */
    private Integer auditType;
    
    /**
     * 敏感词匹配结果
     */
    private String sensitiveWords;
    
    /**
     * 审核分数（0-100，分数越高越安全）
     */
    private Integer auditScore;
    
    /**
     * 审核时间
     */
    private java.time.LocalDateTime auditTime;
    
    /**
     * 是否需要人工复审
     */
    private Integer needManualReview;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getContentType() {
        return contentType;
    }
    
    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }
    
    public Long getContentId() {
        return contentId;
    }
    
    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getAuditStatus() {
        return auditStatus;
    }
    
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    
    public String getAuditResult() {
        return auditResult;
    }
    
    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }
    
    public Long getAuditorId() {
        return auditorId;
    }
    
    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }
    
    public Integer getAuditType() {
        return auditType;
    }
    
    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }
    
    public String getSensitiveWords() {
        return sensitiveWords;
    }
    
    public void setSensitiveWords(String sensitiveWords) {
        this.sensitiveWords = sensitiveWords;
    }
    
    public Integer getAuditScore() {
        return auditScore;
    }
    
    public void setAuditScore(Integer auditScore) {
        this.auditScore = auditScore;
    }
    
    public java.time.LocalDateTime getAuditTime() {
        return auditTime;
    }
    
    public void setAuditTime(java.time.LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }
    
    public Integer getNeedManualReview() {
        return needManualReview;
    }
    
    public void setNeedManualReview(Integer needManualReview) {
        this.needManualReview = needManualReview;
    }
}