package com.example.nettyim.entity.enums;

/**
 * 支付状态枚举
 */
public enum PaymentStatus {
    
    /**
     * 待支付
     */
    PENDING(0, "待支付"),
    
    /**
     * 支付中
     */
    PROCESSING(1, "支付中"),
    
    /**
     * 支付成功
     */
    SUCCESS(2, "支付成功"),
    
    /**
     * 支付失败
     */
    FAILED(3, "支付失败"),
    
    /**
     * 已退款
     */
    REFUNDED(4, "已退款"),
    
    /**
     * 已取消
     */
    CANCELLED(5, "已取消");
    
    private final Integer code;
    private final String description;
    
    PaymentStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static PaymentStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        
        for (PaymentStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        
        return null;
    }
    
    /**
     * 是否为最终状态（不可再变更）
     */
    public boolean isFinalStatus() {
        return this == SUCCESS || this == FAILED || this == REFUNDED || this == CANCELLED;
    }
    
    /**
     * 是否为成功状态
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }
}