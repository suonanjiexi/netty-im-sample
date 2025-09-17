package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 消息查询DTO
 */
@Data
public class MessageQueryDTO {
    
    /**
     * 会话对象ID（用户ID或群组ID）
     */
    @NotNull(message = "会话对象ID不能为空")
    private Long targetId;
    
    /**
     * 会话类型：1-私聊，2-群聊
     */
    @NotNull(message = "会话类型不能为空")
    private Integer conversationType;
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 20;
    
    /**
     * 最后一条消息ID（用于分页）
     */
    private Long lastMessageId;
    
    // Getter and Setter methods
    public Long getTargetId() {
        return targetId;
    }
    
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    
    public Integer getConversationType() {
        return conversationType;
    }
    
    public void setConversationType(Integer conversationType) {
        this.conversationType = conversationType;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public Long getLastMessageId() {
        return lastMessageId;
    }
    
    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}