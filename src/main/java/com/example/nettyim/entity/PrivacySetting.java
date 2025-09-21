package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 隐私设置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("privacy_settings")
public class PrivacySetting extends BaseEntity {
    
    /**
     * 隐私设置ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 设置键
     */
    private String settingKey;
    
    /**
     * 设置值
     */
    private String settingValue;
    
    /**
     * 设置描述
     */
    private String description;
}