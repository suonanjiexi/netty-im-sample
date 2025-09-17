package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 朋友圈评论实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("moment_comments")
public class MomentComment extends BaseEntity {
    
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 动态ID
     */
    private Long momentId;
    
    /**
     * 评论用户ID
     */
    private Long userId;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 回复的用户ID
     */
    private Long replyToUserId;
    
    /**
     * 回复的评论ID
     */
    private Long replyToCommentId;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMomentId() {
        return momentId;
    }
    
    public void setMomentId(Long momentId) {
        this.momentId = momentId;
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
    
    public Long getReplyToUserId() {
        return replyToUserId;
    }
    
    public void setReplyToUserId(Long replyToUserId) {
        this.replyToUserId = replyToUserId;
    }
    
    public Long getReplyToCommentId() {
        return replyToCommentId;
    }
    
    public void setReplyToCommentId(Long replyToCommentId) {
        this.replyToCommentId = replyToCommentId;
    }
}