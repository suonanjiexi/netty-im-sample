package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 添加好友DTO
 */
@Data
public class AddFriendDTO {
    
    @NotNull(message = "好友ID不能为空")
    private Long friendId;
    
    @Size(max = 50, message = "备注名不能超过50个字符")
    private String remark;
    
    @Size(max = 200, message = "申请消息不能超过200个字符")
    private String message;
    
    // Getter and Setter methods
    public Long getFriendId() {
        return friendId;
    }
    
    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}

/**
 * 处理好友申请DTO
 */
@Data
class HandleFriendRequestDTO {
    
    @NotNull(message = "申请ID不能为空")
    private Long requestId;
    
    @NotNull(message = "处理结果不能为空")
    private Integer action; // 1-同意，2-拒绝
    
    @Size(max = 50, message = "备注名不能超过50个字符")
    private String remark;
    
    // Getter and Setter methods
    public Long getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    
    public Integer getAction() {
        return action;
    }
    
    public void setAction(Integer action) {
        this.action = action;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}

/**
 * 更新好友备注DTO
 */
@Data
class UpdateFriendRemarkDTO {
    
    @NotNull(message = "好友ID不能为空")
    private Long friendId;
    
    @Size(max = 50, message = "备注名不能超过50个字符")
    private String remark;
    
    // Getter and Setter methods
    public Long getFriendId() {
        return friendId;
    }
    
    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}