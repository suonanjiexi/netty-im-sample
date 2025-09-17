package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 回帖DTO
 */
@Data
public class ForumReplyDTO {
    
    @NotNull(message = "帖子ID不能为空")
    private Long postId;
    
    @NotBlank(message = "回复内容不能为空")
    @Size(max = 1000, message = "回复内容不能超过1000字符")
    private String content;
    
    /**
     * 图片URL列表
     */
    private List<String> images;
    
    /**
     * 回复的用户ID
     */
    private Long replyToUserId;
    
    /**
     * 回复的回复ID
     */
    private Long replyToReplyId;
    
    // Getter and Setter methods
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
    
    public Long getReplyToUserId() {
        return replyToUserId;
    }
    
    public void setReplyToUserId(Long replyToUserId) {
        this.replyToUserId = replyToUserId;
    }
    
    public Long getReplyToReplyId() {
        return replyToReplyId;
    }
    
    public void setReplyToReplyId(Long replyToReplyId) {
        this.replyToReplyId = replyToReplyId;
    }
}