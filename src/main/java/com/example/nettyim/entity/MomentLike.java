package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 朋友圈点赞实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("moment_likes")
public class MomentLike extends BaseEntity {
    
    /**
     * 点赞ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 动态ID
     */
    private Long momentId;
    
    /**
     * 点赞用户ID
     */
    private Long userId;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMomentId() {
        return momentId;
    }
    
    public void setMomentId(Long momentId) {
        this.momentId = momentId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}