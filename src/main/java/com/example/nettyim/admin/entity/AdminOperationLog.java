package com.example.nettyim.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.nettyim.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 管理员操作日志实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("admin_operation_logs")
public class AdminOperationLog extends BaseEntity {
    
    /**
     * 管理员ID
     */
    @TableField("admin_user_id")
    private Long adminUserId;
    
    /**
     * 管理员用户名
     */
    @TableField("admin_username")
    private String adminUsername;
    
    /**
     * 操作类型
     */
    @TableField("operation")
    private String operation;
    
    /**
     * 操作模块
     */
    @TableField("module")
    private String module;
    
    /**
     * 操作描述
     */
    @TableField("description")
    private String description;
    
    /**
     * 请求方法
     */
    @TableField("request_method")
    private String requestMethod;
    
    /**
     * 请求URL
     */
    @TableField("request_url")
    private String requestUrl;
    
    /**
     * 请求参数
     */
    @TableField("request_params")
    private String requestParams;
    
    /**
     * 响应结果
     */
    @TableField("response_result")
    private String responseResult;
    
    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;
    
    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;
    
    /**
     * 执行时间（毫秒）
     */
    @TableField("execution_time")
    private Integer executionTime;
}