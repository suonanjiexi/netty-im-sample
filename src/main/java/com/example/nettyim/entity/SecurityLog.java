package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("security_logs")
public class SecurityLog extends BaseEntity {
    
    /**
     * 日志ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 操作类型
     */
    private String actionType;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 地理位置
     */
    private String location;
    
    /**
     * 设备信息（JSON格式）
     */
    private String deviceInfo;
    
    /**
     * 结果：0-失败，1-成功
     */
    private Integer result;
    
    /**
     * 风险等级：0-低，1-中，2-高
     */
    private Integer riskLevel;
    
    /**
     * 描述
     */
    private String description;
}