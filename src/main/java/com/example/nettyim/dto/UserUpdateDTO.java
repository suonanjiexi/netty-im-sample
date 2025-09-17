package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 用户信息更新DTO
 */
@Data
public class UserUpdateDTO {
    
    @Size(min = 1, max = 50, message = "昵称长度必须在1-50之间")
    private String nickname;
    
    private String avatar;
    
    private Integer onlineStatus;
    
    // Getter and Setter methods
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Integer getOnlineStatus() {
        return onlineStatus;
    }
    
    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}