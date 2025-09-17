package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 发送消息DTO
 */
@Data
public class SendMessageDTO {
    
    /**
     * 接收者ID（私聊时使用）
     */
    private Long toUserId;
    
    /**
     * 群组ID（群聊时使用）
     */
    private Long groupId;
    
    /**
     * 消息类型：1-文本，2-图片，3-文件，4-语音，5-视频
     */
    @NotNull(message = "消息类型不能为空")
    private Integer messageType;
    
    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 5000, message = "消息内容不能超过5000个字符")
    private String content;
    
    /**
     * 文件URL（非文本消息）
     */
    private String fileUrl;
    
    /**
     * 文件大小
     */
    private Long fileSize;
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 回复的消息ID
     */
    private String replyToMessageId;
    
    // Getter and Setter methods
    public Long getToUserId() {
        return toUserId;
    }
    
    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
    
    public Long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public Integer getMessageType() {
        return messageType;
    }
    
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getReplyToMessageId() {
        return replyToMessageId;
    }
    
    public void setReplyToMessageId(String replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }
}