package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 朋友圈评论DTO
 */
@Data
public class MomentCommentDTO {
    
    @NotNull(message = "动态ID不能为空")
    private Long momentId;
    
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论内容不能超过500字符")
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
    public Long getMomentId() {
        return momentId;
    }
    
    public void setMomentId(Long momentId) {
        this.momentId = momentId;
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