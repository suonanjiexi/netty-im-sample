package com.example.nettyim.admin.dto;

import lombok.Data;

/**
 * 管理员登录响应DTO
 */
@Data
public class AdminLoginResponseDTO {
    
    /**
     * 管理员ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 角色
     */
    private String role;
    
    /**
     * 访问令牌
     */
    private String token;
}