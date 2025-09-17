package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录DTO
 */
@Data
public class UserLoginDTO {
    
    @NotBlank(message = "登录账号不能为空")
    private String account; // 可以是用户名或手机号
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    // Getter and Setter methods
    public String getAccount() {
        return account;
    }
    
    public void setAccount(String account) {
        this.account = account;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}