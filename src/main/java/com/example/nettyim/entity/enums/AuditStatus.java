package com.example.nettyim.entity.enums;

/**
 * 审核状态枚举
 */
public enum AuditStatus {
    PENDING(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核拒绝"),
    AUTO_APPROVED(3, "自动通过"),
    AUTO_REJECTED(4, "自动拒绝");
    
    private final Integer code;
    private final String description;
    
    AuditStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static AuditStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AuditStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}