package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 贴吧帖子实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forum_posts")
public class ForumPost extends BaseEntity {
    
    /**
     * 帖子ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 贴吧ID
     */
    private Long forumId;
    
    /**
     * 发帖用户ID
     */
    private Long userId;
    
    /**
     * 帖子标题
     */
    private String title;
    
    /**
     * 帖子内容
     */
    private String content;
    
    /**
     * 图片URL列表（JSON格式）
     */
    private String images;
    
    /**
     * 帖子分类
     */
    private String category;
    
    /**
     * 浏览数
     */
    private Integer viewCount;
    
    /**
     * 回复数
     */
    private Integer replyCount;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 是否置顶：0-普通，1-置顶
     */
    private Integer isPinned;
    
    /**
     * 是否精华：0-普通，1-精华
     */
    private Integer isEssence;
    
    /**
     * 状态：0-删除，1-正常
     */
    private Integer status;
    
    /**
     * 最后回复时间
     */
    private LocalDateTime lastReplyTime;
    
    /**
     * 审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝
     */
    private Integer auditStatus;
    
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public Integer getReplyCount() {
        return replyCount;
    }
    
    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }
    
    public Integer getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
    
    public Integer getIsPinned() {
        return isPinned;
    }
    
    public void setIsPinned(Integer isPinned) {
        this.isPinned = isPinned;
    }
    
    public Integer getIsEssence() {
        return isEssence;
    }
    
    public void setIsEssence(Integer isEssence) {
        this.isEssence = isEssence;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getLastReplyTime() {
        return lastReplyTime;
    }
    
    public void setLastReplyTime(LocalDateTime lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }
    
    public Integer getAuditStatus() {
        return auditStatus;
    }
    
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
}