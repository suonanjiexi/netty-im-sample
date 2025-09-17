package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户会话状态实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_sessions")
public class UserSession extends BaseEntity {
    
    /**
     * 会话ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * Socket会话ID
     */
    private String sessionId;
    
    /**
     * 设备类型
     */
    private String deviceType;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    
    /**
     * 最后活跃时间
     */
    private LocalDateTime lastActiveTime;
    
    /**
     * 状态：0-离线，1-在线
     */
    private Integer status;
}