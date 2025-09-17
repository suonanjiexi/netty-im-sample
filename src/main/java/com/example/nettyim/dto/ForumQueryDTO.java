package com.example.nettyim.dto;

import lombok.Data;

/**
 * 贴吧查询DTO
 */
@Data
public class ForumQueryDTO {
    
    /**
     * 页码，默认为1
     */
    private Integer page = 1;
    
    /**
     * 每页大小，默认为10
     */
    private Integer size = 10;
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 分类
     */
    private String category;
    
    /**
     * 是否公开
     */
    private Integer isPublic;
    
    /**
     * 吧主ID
     */
    private Long ownerId;
    
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
    
    public Integer getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }
    
    public Long getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}