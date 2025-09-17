package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 朋友圈动态实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("moments")
public class Moment extends BaseEntity {
    
    /**
     * 动态ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 发布用户ID
     */
    private Long userId;
    
    /**
     * 动态内容
     */
    private String content;
    
    /**
     * 图片URL列表（JSON格式存储）
     */
    private String images;
    
    /**
     * 位置信息
     */
    private String location;
    
    /**
     * 可见性：0-公开，1-仅好友，2-仅自己
     */
    private Integer visibility;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Integer getVisibility() {
        return visibility;
    }
    
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }
    
    public Integer getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
    
    public Integer getCommentCount() {
        return commentCount;
    }
    
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}