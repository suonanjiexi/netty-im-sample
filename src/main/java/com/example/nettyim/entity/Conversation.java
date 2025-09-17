package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会话实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("conversations")
public class Conversation extends BaseEntity {
    
    /**
     * 会话ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 目标ID（好友ID或群组ID）
     */
    private Long targetId;
    
    /**
     * 会话类型：1-私聊，2-群聊
     */
    private Integer conversationType;
    
    /**
     * 最后一条消息ID
     */
    private Long lastMessageId;
    
    /**
     * 最后一条消息内容
     */
    private String lastMessageContent;
    
    /**
     * 最后一条消息时间
     */
    private LocalDateTime lastMessageTime;
    
    /**
     * 未读消息数
     */
    private Integer unreadCount;
    
    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;
    
    /**
     * 是否免打扰：0-否，1-是
     */
    private Integer isMuted;
    
    /**
     * 是否删除：0-否，1-是
     */
    private Integer isDeleted;
}