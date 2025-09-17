package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 贴吧回复点赞实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forum_reply_likes")
public class ForumReplyLike extends BaseEntity {
    
    /**
     * 点赞ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 回复ID
     */
    private Long replyId;
    
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
    
    public Long getReplyId() {
        return replyId;
    }
    
    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}