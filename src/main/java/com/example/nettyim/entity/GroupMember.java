package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 群组成员实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("group_members")
public class GroupMember extends BaseEntity {
    
    /**
     * 成员关系ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 群组ID
     */
    private Long groupId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 角色：0-普通成员，1-管理员，2-群主
     */
    private Integer role;
    
    /**
     * 加入时间
     */
    private LocalDateTime joinTime;
    
    /**
     * 禁言到期时间
     */
    private LocalDateTime muteUntil;
    
    /**
     * 状态：0-已退出，1-正常
     */
    private Integer status;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getRole() {
        return role;
    }
    
    public void setRole(Integer role) {
        this.role = role;
    }
    
    public LocalDateTime getJoinTime() {
        return joinTime;
    }
    
    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }
    
    public LocalDateTime getMuteUntil() {
        return muteUntil;
    }
    
    public void setMuteUntil(LocalDateTime muteUntil) {
        this.muteUntil = muteUntil;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
}