package com.example.nettyim.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.nettyim.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 管理员用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("admin_users")
public class AdminUser extends BaseEntity {
    
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
    
    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;
    
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 角色：ADMIN-超级管理员，OPERATOR-普通运营
     */
    @TableField("role")
    private String role;
    
    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;
}