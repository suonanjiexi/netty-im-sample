package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 贴吧成员实体
 */
@Data
@TableName("forum_members")
public class ForumMember {
    
    /**
     * 成员ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 贴吧ID
     */
    private Long forumId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 角色：0-普通成员，1-管理员，2-吧主
     */
    private Integer role;
    
    /**
     * 加入时间
     */
    private LocalDateTime joinTime;
    
    /**
     * 状态：0-禁言，1-正常
     */
    private Integer status;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getForumId() {
        return forumId;
    }
    
    public void setForumId(Long forumId) {
        this.forumId = forumId;
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
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
}