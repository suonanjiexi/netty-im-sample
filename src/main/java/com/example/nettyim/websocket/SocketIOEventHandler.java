package com.example.nettyim.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.nettyim.dto.SendMessageDTO;
import com.example.nettyim.entity.Message;
import com.example.nettyim.service.MessageService;
import com.example.nettyim.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * WebSocket事件处理器
 */
@Component
public class SocketIOEventHandler {
    
    private static final Logger log = LoggerFactory.getLogger(SocketIOEventHandler.class);
    
    private final SocketIOServer socketIOServer;
    private final MessageService messageService;
    private final UserService userService;
    private final SocketIOServerManager socketIOServerManager;
    
    public SocketIOEventHandler(SocketIOServer socketIOServer, MessageService messageService,
                               UserService userService, SocketIOServerManager socketIOServerManager) {
        this.socketIOServer = socketIOServer;
        this.messageService = messageService;
        this.userService = userService;
        this.socketIOServerManager = socketIOServerManager;
    }
    
    @PostConstruct
    public void initEventHandlers() {
        // 注册事件监听器
        socketIOServer.addEventListener("send_message", SendMessageData.class, this::onSendMessage);
        socketIOServer.addEventListener("join_room", JoinRoomData.class, this::onJoinRoom);
        socketIOServer.addEventListener("leave_room", LeaveRoomData.class, this::onLeaveRoom);
        socketIOServer.addEventListener("typing", TypingData.class, this::onTyping);
        socketIOServer.addEventListener("stop_typing", TypingData.class, this::onStopTyping);
        socketIOServer.addEventListener("mark_read", MarkReadData.class, this::onMarkRead);
        socketIOServer.addEventListener("get_online_users", Object.class, this::onGetOnlineUsers);
        
        log.info("SocketIO事件处理器初始化完成");
    }
    
    /**
     * 处理发送消息事件
     */
    private void onSendMessage(SocketIOClient client, SendMessageData data, AckRequest ackRequest) {
        try {
            // 获取发送者用户ID
            String userIdStr = client.getHandshakeData().getSingleUrlParam("userId");
            if (userIdStr == null) {
                log.warn("发送消息时未找到用户ID");
                return;
            }
            
            Long fromUserId = Long.parseLong(userIdStr);
            
            // 创建发送消息DTO
            SendMessageDTO sendMessageDTO = new SendMessageDTO();
            sendMessageDTO.setToUserId(data.getToUserId());
            sendMessageDTO.setGroupId(data.getGroupId());
            sendMessageDTO.setMessageType(data.getMessageType());
            sendMessageDTO.setContent(data.getContent());
            sendMessageDTO.setFileUrl(data.getFileUrl());
            sendMessageDTO.setFileSize(data.getFileSize());
            sendMessageDTO.setFileName(data.getFileName());
            sendMessageDTO.setReplyToMessageId(data.getReplyToMessageId());
            
            // 发送消息
            Message message = messageService.sendMessage(fromUserId, sendMessageDTO);
            
            // 确认消息发送成功
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new MessageResponse(true, "消息发送成功", message));
            }
            
            log.info("用户 {} 发送消息成功: {}", fromUserId, message.getMessageId());
            
        } catch (Exception e) {
            log.error("处理发送消息事件失败", e);
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new MessageResponse(false, e.getMessage(), null));
            }
        }
    }
    
    /**
     * 处理加入房间事件
     */
    private void onJoinRoom(SocketIOClient client, JoinRoomData data, AckRequest ackRequest) {
        try {
            String roomName = data.getRoomType() == 1 ? 
                "private_" + data.getTargetId() : 
                "group_" + data.getTargetId();
            
            client.joinRoom(roomName);
            log.info("客户端 {} 加入房间: {}", client.getSessionId(), roomName);
            
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new BaseResponse(true, "加入房间成功"));
            }
        } catch (Exception e) {
            log.error("处理加入房间事件失败", e);
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new BaseResponse(false, e.getMessage()));
            }
        }
    }
    
    /**
     * 处理离开房间事件
     */
    private void onLeaveRoom(SocketIOClient client, LeaveRoomData data, AckRequest ackRequest) {
        try {
            String roomName = data.getRoomType() == 1 ? 
                "private_" + data.getTargetId() : 
                "group_" + data.getTargetId();
            
            client.leaveRoom(roomName);
            log.info("客户端 {} 离开房间: {}", client.getSessionId(), roomName);
            
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new BaseResponse(true, "离开房间成功"));
            }
        } catch (Exception e) {
            log.error("处理离开房间事件失败", e);
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new BaseResponse(false, e.getMessage()));
            }
        }
    }
    
    /**
     * 处理正在输入事件
     */
    private void onTyping(SocketIOClient client, TypingData data, AckRequest ackRequest) {
        try {
            String userIdStr = client.getHandshakeData().getSingleUrlParam("userId");
            if (userIdStr == null) return;
            
            Long userId = Long.parseLong(userIdStr);
            
            // 通知对方正在输入
            if (data.getTargetType() == 1) {
                // 私聊
                socketIOServerManager.sendToUser(data.getTargetId(), "user_typing", 
                    new TypingNotification(userId, data.getTargetId(), true));
            } else {
                // 群聊
                socketIOServer.getRoomOperations("group_" + data.getTargetId())
                    .sendEvent("user_typing", new TypingNotification(userId, data.getTargetId(), true));
            }
            
        } catch (Exception e) {
            log.error("处理正在输入事件失败", e);
        }
    }
    
    /**
     * 处理停止输入事件
     */
    private void onStopTyping(SocketIOClient client, TypingData data, AckRequest ackRequest) {
        try {
            String userIdStr = client.getHandshakeData().getSingleUrlParam("userId");
            if (userIdStr == null) return;
            
            Long userId = Long.parseLong(userIdStr);
            
            // 通知对方停止输入
            if (data.getTargetType() == 1) {
                // 私聊
                socketIOServerManager.sendToUser(data.getTargetId(), "user_stop_typing", 
                    new TypingNotification(userId, data.getTargetId(), false));
            } else {
                // 群聊
                socketIOServer.getRoomOperations("group_" + data.getTargetId())
                    .sendEvent("user_stop_typing", new TypingNotification(userId, data.getTargetId(), false));
            }
            
        } catch (Exception e) {
            log.error("处理停止输入事件失败", e);
        }
    }
    
    /**
     * 处理标记已读事件
     */
    private void onMarkRead(SocketIOClient client, MarkReadData data, AckRequest ackRequest) {
        try {
            String userIdStr = client.getHandshakeData().getSingleUrlParam("userId");
            if (userIdStr == null) return;
            
            Long userId = Long.parseLong(userIdStr);
            
            if (data.getMessageIds() != null && !data.getMessageIds().isEmpty()) {
                messageService.markMessagesAsRead(userId, data.getMessageIds());
            }
            
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new BaseResponse(true, "消息已标记为已读"));
            }
            
        } catch (Exception e) {
            log.error("处理标记已读事件失败", e);
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new BaseResponse(false, e.getMessage()));
            }
        }
    }
    
    /**
     * 处理获取在线用户事件
     */
    private void onGetOnlineUsers(SocketIOClient client, Object data, AckRequest ackRequest) {
        try {
            java.util.Set<Long> onlineUserIds = socketIOServerManager.getOnlineUserIds();
            
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new OnlineUsersResponse(true, "获取成功", onlineUserIds));
            }
            
        } catch (Exception e) {
            log.error("处理获取在线用户事件失败", e);
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(new OnlineUsersResponse(false, e.getMessage(), null));
            }
        }
    }
    
    // 内部数据类
    public static class SendMessageData {
        private Long toUserId;
        private Long groupId;
        private Integer messageType;
        private String content;
        private String fileUrl;
        private Long fileSize;
        private String fileName;
        private String replyToMessageId;
        
        // Getters and Setters
        public Long getToUserId() { return toUserId; }
        public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
        public Long getGroupId() { return groupId; }
        public void setGroupId(Long groupId) { this.groupId = groupId; }
        public Integer getMessageType() { return messageType; }
        public void setMessageType(Integer messageType) { this.messageType = messageType; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
        public Long getFileSize() { return fileSize; }
        public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public String getReplyToMessageId() { return replyToMessageId; }
        public void setReplyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; }
    }
    
    public static class JoinRoomData {
        private Long targetId;
        private Integer roomType; // 1-私聊，2-群聊
        
        public Long getTargetId() { return targetId; }
        public void setTargetId(Long targetId) { this.targetId = targetId; }
        public Integer getRoomType() { return roomType; }
        public void setRoomType(Integer roomType) { this.roomType = roomType; }
    }
    
    public static class LeaveRoomData {
        private Long targetId;
        private Integer roomType;
        
        public Long getTargetId() { return targetId; }
        public void setTargetId(Long targetId) { this.targetId = targetId; }
        public Integer getRoomType() { return roomType; }
        public void setRoomType(Integer roomType) { this.roomType = roomType; }
    }
    
    public static class TypingData {
        private Long targetId;
        private Integer targetType;
        
        public Long getTargetId() { return targetId; }
        public void setTargetId(Long targetId) { this.targetId = targetId; }
        public Integer getTargetType() { return targetType; }
        public void setTargetType(Integer targetType) { this.targetType = targetType; }
    }
    
    public static class MarkReadData {
        private java.util.List<Long> messageIds;
        
        public java.util.List<Long> getMessageIds() { return messageIds; }
        public void setMessageIds(java.util.List<Long> messageIds) { this.messageIds = messageIds; }
    }
    
    public static class BaseResponse {
        private boolean success;
        private String message;
        
        public BaseResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    public static class MessageResponse extends BaseResponse {
        private Message data;
        
        public MessageResponse(boolean success, String message, Message data) {
            super(success, message);
            this.data = data;
        }
        
        public Message getData() { return data; }
    }
    
    public static class OnlineUsersResponse extends BaseResponse {
        private java.util.Set<Long> data;
        
        public OnlineUsersResponse(boolean success, String message, java.util.Set<Long> data) {
            super(success, message);
            this.data = data;
        }
        
        public java.util.Set<Long> getData() { return data; }
    }
    
    public static class TypingNotification {
        private Long userId;
        private Long targetId;
        private boolean isTyping;
        
        public TypingNotification(Long userId, Long targetId, boolean isTyping) {
            this.userId = userId;
            this.targetId = targetId;
            this.isTyping = isTyping;
        }
        
        public Long getUserId() { return userId; }
        public Long getTargetId() { return targetId; }
        public boolean isTyping() { return isTyping; }
    }
}