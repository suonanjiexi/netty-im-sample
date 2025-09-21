package com.example.nettyim.service;

import com.example.nettyim.entity.UserKey;
import com.example.nettyim.entity.MessageEncryption;

import java.util.Map;

/**
 * 端到端加密服务接口
 */
public interface EncryptionService {
    
    /**
     * 生成用户密钥对
     */
    UserKey generateUserKeyPair(Long userId, String keyType);
    
    /**
     * 获取用户公钥
     */
    String getUserPublicKey(Long userId);
    
    /**
     * 获取用户活跃密钥
     */
    UserKey getUserActiveKey(Long userId);
    
    /**
     * 加密消息
     */
    String encryptMessage(String message, String publicKey);
    
    /**
     * 解密消息
     */
    String decryptMessage(String encryptedMessage, String privateKey, String keyPassword);
    
    /**
     * 验证密钥
     */
    Boolean verifyKey(String keyId, String publicKey);
    
    /**
     * 轮换用户密钥
     */
    Boolean rotateUserKey(Long userId);
    
    /**
     * 撤销用户密钥
     */
    Boolean revokeUserKey(Long userId, String keyId);
    
    /**
     * 记录消息加密信息
     */
    Boolean recordMessageEncryption(String messageId, String keyId, String algorithm);
    
    /**
     * 获取消息加密信息
     */
    MessageEncryption getMessageEncryptionInfo(String messageId);
    
    /**
     * 检查用户是否支持端到端加密
     */
    Boolean supportsEncryption(Long userId);
    
    /**
     * 获取加密统计信息
     */
    Map<String, Object> getEncryptionStats(Long userId);
}