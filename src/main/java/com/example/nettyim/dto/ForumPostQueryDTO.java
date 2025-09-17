package com.example.nettyim.dto;

import lombok.Data;

/**
 * 贴吧帖子查询DTO
 */
@Data
public class ForumPostQueryDTO {
    
    /**
     * 页码，默认为1
     */
    private Integer page = 1;
    
    /**
     * 每页大小，默认为10
     */
    private Integer size = 10;
    
    /**
     * 贴吧ID
     */
    private Long forumId;
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 帖子分类
     */
    private String category;
    
    /**
     * 发帖用户ID
     */
    private Long userId;
    
    /**
     * 是否置顶
     */
    private Integer isPinned;
    
    /**
     * 是否精华
     */
    private Integer isEssence;
    
    /**
     * 排序方式：create_time, last_reply_time, view_count, reply_count
     */
    private String orderBy = "last_reply_time";
    
    /**
     * 排序方向：asc, desc
     */
    private String orderDirection = "desc";
    
    // Getter and Setter methods
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public Long getForumId() {
        return forumId;
    }
    
    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public String getOrderBy() {
        return orderBy;
    }
    
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public String getOrderDirection() {
        return orderDirection;
    }
    
    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}