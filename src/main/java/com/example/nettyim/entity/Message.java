package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("messages")
public class Message extends BaseEntity {
    
    /**
     * 消息ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 消息唯一标识
     */
    private String messageId;
    
    /**
     * 发送者ID
     */
    private Long fromUserId;
    
    /**
     * 接收者ID（私聊）
     */
    private Long toUserId;
    
    /**
     * 群组ID（群聊）
     */
    private Long groupId;
    
    /**
     * 消息类型：1-文本，2-图片，3-文件，4-语音，5-视频，6-系统消息
     */
    private Integer messageType;
    
    /**
     * 消息内容
     */
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
    
    /**
     * 是否已读：0-未读，1-已读
     */
    private Integer isRead;
    
    /**
     * 是否删除：0-正常，1-已删除
     */
    private Integer isDeleted;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public Long getFromUserId() {
        return fromUserId;
    }
    
    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }
    
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
    
    public Integer getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
    
    public Integer getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}