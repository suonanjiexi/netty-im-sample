package com.example.nettyim.service.impl;

import com.example.nettyim.dto.MessageQueryDTO;
import com.example.nettyim.dto.SendMessageDTO;
import com.example.nettyim.entity.Message;
import com.example.nettyim.exception.BusinessException;
import com.example.nettyim.mapper.MessageMapper;
import com.example.nettyim.service.MessageService;
import com.example.nettyim.service.FriendshipService;
import com.example.nettyim.service.GroupService;
import com.example.nettyim.websocket.SocketIOServerManager;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 消息服务实现
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    
    private final MessageMapper messageMapper;
    private final FriendshipService friendshipService;
    private final GroupService groupService;
    private final SocketIOServerManager socketIOServerManager;
    
    public MessageServiceImpl(MessageMapper messageMapper, FriendshipService friendshipService, 
                             GroupService groupService, SocketIOServerManager socketIOServerManager) {
        this.messageMapper = messageMapper;
        this.friendshipService = friendshipService;
        this.groupService = groupService;
        this.socketIOServerManager = socketIOServerManager;
    }
    
    @Override
    public Message sendMessage(Long fromUserId, SendMessageDTO sendMessageDTO) {
        // 验证消息类型和内容
        if (sendMessageDTO.getMessageType() == null || 
            (sendMessageDTO.getContent() == null || sendMessageDTO.getContent().trim().isEmpty())) {
            throw new BusinessException("消息内容不能为空");
        }
        
        // 检查是私聊还是群聊
        if (sendMessageDTO.getToUserId() != null && sendMessageDTO.getGroupId() != null) {
            throw new BusinessException("不能同时指定私聊用户和群组");
        }
        
        if (sendMessageDTO.getToUserId() == null && sendMessageDTO.getGroupId() == null) {
            throw new BusinessException("必须指定接收者或群组");
        }
        
        // 验证权限
        if (sendMessageDTO.getToUserId() != null) {
            // 私聊：检查是否为好友
            if (!friendshipService.isFriend(fromUserId, sendMessageDTO.getToUserId())) {
                throw new BusinessException("只能向好友发送消息");
            }
        } else {
            // 群聊：检查是否为群成员
            if (!groupService.isMember(fromUserId, sendMessageDTO.getGroupId())) {
                throw new BusinessException("只有群成员才能发送群消息");
            }
        }
        
        // 创建消息
        Message message = new Message();
        message.setMessageId(UUID.randomUUID().toString());
        message.setFromUserId(fromUserId);
        message.setToUserId(sendMessageDTO.getToUserId());
        message.setGroupId(sendMessageDTO.getGroupId());
        message.setMessageType(sendMessageDTO.getMessageType());
        message.setContent(sendMessageDTO.getContent());
        message.setFileUrl(sendMessageDTO.getFileUrl());
        message.setFileSize(sendMessageDTO.getFileSize());
        message.setFileName(sendMessageDTO.getFileName());
        message.setReplyToMessageId(sendMessageDTO.getReplyToMessageId());
        message.setIsRead(0);
        message.setIsDeleted(0);
        
        messageMapper.insert(message);
        
        // 实时推送消息
        if (sendMessageDTO.getToUserId() != null) {
            // 私聊消息推送
            socketIOServerManager.sendToUser(sendMessageDTO.getToUserId(), "private_message", message);
        } else {
            // 群聊消息推送
            List<com.example.nettyim.entity.User> members = groupService.getGroupMembers(sendMessageDTO.getGroupId());
            List<Long> memberIds = members.stream()
                    .map(com.example.nettyim.entity.User::getId)
                    .filter(id -> !id.equals(fromUserId)) // 排除发送者
                    .toList();
            
            socketIOServerManager.sendToGroup(memberIds, "group_message", message);
        }
        
        return message;
    }
    
    @Override
    public Page<Message> getPrivateMessages(MessageQueryDTO queryDTO) {
        if (queryDTO.getConversationType() != 1) {
            throw new BusinessException("会话类型错误");
        }
        
        Page<Message> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>()
                .and(w -> w
                    .and(w1 -> w1.eq("from_user_id", queryDTO.getTargetId()).eq("to_user_id", queryDTO.getTargetId()))
                    .or(w2 -> w2.eq("from_user_id", queryDTO.getTargetId()).eq("to_user_id", queryDTO.getTargetId()))
                )
                .eq("is_deleted", 0)
                .orderByDesc("created_at");
        
        if (queryDTO.getLastMessageId() != null) {
            wrapper.lt("id", queryDTO.getLastMessageId());
        }
        
        return messageMapper.selectPage(page, wrapper);
    }
    
    @Override
    public Page<Message> getGroupMessages(MessageQueryDTO queryDTO) {
        if (queryDTO.getConversationType() != 2) {
            throw new BusinessException("会话类型错误");
        }
        
        // 检查是否为群成员
        if (!groupService.isMember(queryDTO.getTargetId(), queryDTO.getTargetId())) {
            throw new BusinessException("只有群成员才能查看群消息");
        }
        
        Page<Message> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>()
                .eq("group_id", queryDTO.getTargetId())
                .eq("is_deleted", 0)
                .orderByDesc("created_at");
        
        if (queryDTO.getLastMessageId() != null) {
            wrapper.lt("id", queryDTO.getLastMessageId());
        }
        
        return messageMapper.selectPage(page, wrapper);
    }
    
    @Override
    public void markMessageAsRead(Long userId, Long messageId) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        
        // 只有接收者可以标记消息为已读
        if (message.getToUserId() != null && !message.getToUserId().equals(userId)) {
            throw new BusinessException("无权限标记该消息为已读");
        }
        
        if (message.getGroupId() != null && !groupService.isMember(userId, message.getGroupId())) {
            throw new BusinessException("无权限标记该消息为已读");
        }
        
        message.setIsRead(1);
        messageMapper.updateById(message);
    }
    
    @Override
    public void markMessagesAsRead(Long userId, List<Long> messageIds) {
        for (Long messageId : messageIds) {
            try {
                markMessageAsRead(userId, messageId);
            } catch (Exception e) {
                // 忽略单个消息标记失败，继续处理其他消息
            }
        }
    }
    
    @Override
    public void deleteMessage(Long userId, Long messageId) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        
        // 只有发送者可以删除消息
        if (!message.getFromUserId().equals(userId)) {
            throw new BusinessException("只能删除自己发送的消息");
        }
        
        message.setIsDeleted(1);
        messageMapper.updateById(message);
        
        // 通知相关用户消息已删除
        if (message.getToUserId() != null) {
            socketIOServerManager.sendToUser(message.getToUserId(), "message_deleted", messageId);
        } else if (message.getGroupId() != null) {
            List<com.example.nettyim.entity.User> members = groupService.getGroupMembers(message.getGroupId());
            List<Long> memberIds = members.stream()
                    .map(com.example.nettyim.entity.User::getId)
                    .filter(id -> !id.equals(userId))
                    .toList();
            
            socketIOServerManager.sendToGroup(memberIds, "message_deleted", messageId);
        }
    }
    
    @Override
    public int getUnreadMessageCount(Long userId, Long targetId, Integer conversationType) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        
        if (conversationType == 1) {
            // 私聊未读消息
            wrapper.eq("from_user_id", targetId)
                   .eq("to_user_id", userId)
                   .eq("is_read", 0)
                   .eq("is_deleted", 0);
        } else if (conversationType == 2) {
            // 群聊未读消息（简化处理，实际应该记录每个用户的阅读状态）
            wrapper.eq("group_id", targetId)
                   .ne("from_user_id", userId)
                   .eq("is_deleted", 0);
        } else {
            throw new BusinessException("无效的会话类型");
        }
        
        return Math.toIntExact(messageMapper.selectCount(wrapper));
    }
    
    @Override
    public Message getMessageById(Long messageId) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        return message;
    }
    
    @Override
    public void recallMessage(Long userId, Long messageId) {
        Message message = messageMapper.selectById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        
        // 只有发送者可以撤回消息
        if (!message.getFromUserId().equals(userId)) {
            throw new BusinessException("只能撤回自己发送的消息");
        }
        
        // 检查撤回时间限制（例如：只能撤回2分钟内的消息）
        if (message.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(2))) {
            throw new BusinessException("消息发送超过2分钟，无法撤回");
        }
        
        // 更新消息内容为撤回状态
        message.setContent("[此消息已被撤回]");
        message.setMessageType(6); // 系统消息
        messageMapper.updateById(message);
        
        // 通知相关用户消息已撤回
        if (message.getToUserId() != null) {
            socketIOServerManager.sendToUser(message.getToUserId(), "message_recalled", message);
        } else if (message.getGroupId() != null) {
            List<com.example.nettyim.entity.User> members = groupService.getGroupMembers(message.getGroupId());
            List<Long> memberIds = members.stream()
                    .map(com.example.nettyim.entity.User::getId)
                    .filter(id -> !id.equals(userId))
                    .toList();
            
            socketIOServerManager.sendToGroup(memberIds, "message_recalled", message);
        }
    }
}