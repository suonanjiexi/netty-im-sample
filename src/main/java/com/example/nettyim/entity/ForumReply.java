package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 贴吧回复实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forum_replies")
public class ForumReply extends BaseEntity {
    
    /**
     * 回复ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 回复用户ID
     */
    private Long userId;
    
    /**
     * 回复内容
     */
    private String content;
    
    /**
     * 图片URL列表（JSON格式）
     */
    private String images;
    
    /**
     * 回复的用户ID
     */
    private Long replyToUserId;
    
    /**
     * 回复的回复ID
     */
    private Long replyToReplyId;
    
    /**
     * 楼层号
     */
    private Integer floorNumber;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 状态：0-删除，1-正常
     */
    private Integer status;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getImages() {
        return images;
    }
    
    public void setImages(String images) {
        this.images = images;
    }
    
    public Long getReplyToUserId() {
        return replyToUserId;
    }
    
    public void setReplyToUserId(Long replyToUserId) {
        this.replyToUserId = replyToUserId;
    }
    
    public Long getReplyToReplyId() {
        return replyToReplyId;
    }
    
    public void setReplyToReplyId(Long replyToReplyId) {
        this.replyToReplyId = replyToReplyId;
    }
    
    public Integer getFloorNumber() {
        return floorNumber;
    }
    
    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
    
    public Integer getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
}