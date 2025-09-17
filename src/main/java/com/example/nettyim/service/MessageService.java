package com.example.nettyim.service;

import com.example.nettyim.dto.MessageQueryDTO;
import com.example.nettyim.dto.SendMessageDTO;
import com.example.nettyim.entity.Message;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 消息服务接口
 */
public interface MessageService {
    
    /**
     * 发送消息
     */
    Message sendMessage(Long fromUserId, SendMessageDTO sendMessageDTO);
    
    /**
     * 获取私聊消息历史
     */
    Page<Message> getPrivateMessages(MessageQueryDTO queryDTO);
    
    /**
     * 获取群聊消息历史
     */
    Page<Message> getGroupMessages(MessageQueryDTO queryDTO);
    
    /**
     * 标记消息为已读
     */
    void markMessageAsRead(Long userId, Long messageId);
    
    /**
     * 批量标记消息为已读
     */
    void markMessagesAsRead(Long userId, List<Long> messageIds);
    
    /**
     * 删除消息
     */
    void deleteMessage(Long userId, Long messageId);
    
    /**
     * 获取未读消息数量
     */
    int getUnreadMessageCount(Long userId, Long targetId, Integer conversationType);
    
    /**
     * 获取消息详情
     */
    Message getMessageById(Long messageId);
    
    /**
     * 撤回消息
     */
    void recallMessage(Long userId, Long messageId);
}