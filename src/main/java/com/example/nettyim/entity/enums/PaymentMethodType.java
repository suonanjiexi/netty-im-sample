package com.example.nettyim.entity.enums;

/**
 * 支付方式类型枚举
 */
public enum PaymentMethodType {
    
    /**
     * 第三方支付
     */
    THIRD_PARTY(1, "第三方支付"),
    
    /**
     * 银行卡支付
     */
    BANK_CARD(2, "银行卡支付"),
    
    /**
     * 余额支付
     */
    WALLET(3, "余额支付");
    
    private final Integer code;
    private final String description;
    
    PaymentMethodType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static PaymentMethodType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        
        for (PaymentMethodType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        
        return null;
    }
}