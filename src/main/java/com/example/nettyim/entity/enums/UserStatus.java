package com.example.nettyim.entity.enums;

/**
 * 用户状态枚举
 */
public enum UserStatus {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");
    
    private final Integer code;
    private final String description;
    
    UserStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

/**
 * 在线状态枚举
 */
enum OnlineStatus {
    OFFLINE(0, "离线"),
    ONLINE(1, "在线"),
    BUSY(2, "忙碌"),
    AWAY(3, "离开");
    
    private final Integer code;
    private final String description;
    
    OnlineStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

/**
 * 好友关系状态枚举
 */
enum FriendshipStatus {
    PENDING(0, "待确认"),
    ACCEPTED(1, "已同意"),
    REJECTED(2, "已拒绝"),
    DELETED(3, "已删除");
    
    private final Integer code;
    private final String description;
    
    FriendshipStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

/**
 * 群组成员角色枚举
 */
enum GroupMemberRole {
    MEMBER(0, "普通成员"),
    ADMIN(1, "管理员"),
    OWNER(2, "群主");
    
    private final Integer code;
    private final String description;
    
    GroupMemberRole(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

/**
 * 消息类型枚举
 */
enum MessageType {
    TEXT(1, "文本"),
    IMAGE(2, "图片"),
    FILE(3, "文件"),
    VOICE(4, "语音"),
    VIDEO(5, "视频"),
    SYSTEM(6, "系统消息");
    
    private final Integer code;
    private final String description;
    
    MessageType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

/**
 * 会话类型枚举
 */
enum ConversationType {
    PRIVATE(1, "私聊"),
    GROUP(2, "群聊");
    
    private final Integer code;
    private final String description;
    
    ConversationType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}