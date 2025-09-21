package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息加密记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("message_encryption")
public class MessageEncryption extends BaseEntity {
    
    /**
     * 加密ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 消息ID
     */
    private String messageId;
    
    /**
     * 加密密钥ID
     */
    private String encryptionKeyId;
    
    /**
     * 加密算法
     */
    private String algorithm;
}