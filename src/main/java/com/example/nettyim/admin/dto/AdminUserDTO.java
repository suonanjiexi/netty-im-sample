package com.example.nettyim.admin.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 管理员用户DTO
 */
@Data
public class AdminUserDTO {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 角色：ADMIN-超级管理员，OPERATOR-普通运营
     */
    @NotBlank(message = "角色不能为空")
    private String role;
    
    /**
     * 状态：0-禁用，1-启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}