package com.example.nettyim.entity.enums;

/**
 * 内容类型枚举
 */
public enum ContentType {
    MESSAGE(1, "消息"),
    MOMENT(2, "朋友圈动态"),
    MOMENT_COMMENT(3, "朋友圈评论"),
    FORUM_POST(4, "贴吧帖子"),
    FORUM_REPLY(5, "贴吧回复"),
    USER_PROFILE(6, "用户资料");
    
    private final Integer code;
    private final String description;
    
    ContentType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static ContentType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ContentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}