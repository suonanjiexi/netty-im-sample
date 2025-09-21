package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户密钥实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_keys")
public class UserKey extends BaseEntity {
    
    /**
     * 密钥ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 密钥标识
     */
    private String keyId;
    
    /**
     * 公钥
     */
    private String publicKey;
    
    /**
     * 加密私钥
     */
    private String privateKeyEncrypted;
    
    /**
     * 密钥类型
     */
    private String keyType;
    
    /**
     * 是否激活
     */
    private Boolean isActive;
    
    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;
    
    /**
     * 检查密钥是否过期
     */
    public Boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
}