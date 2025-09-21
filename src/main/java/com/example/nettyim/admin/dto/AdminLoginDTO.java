package com.example.nettyim.admin.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 管理员登录DTO
 */
@Data
public class AdminLoginDTO {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}