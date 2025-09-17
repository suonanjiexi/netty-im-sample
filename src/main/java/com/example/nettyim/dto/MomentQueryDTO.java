package com.example.nettyim.dto;

import lombok.Data;

/**
 * 朋友圈查询DTO
 */
@Data
public class MomentQueryDTO {
    
    /**
     * 页码，默认为1
     */
    private Integer page = 1;
    
    /**
     * 每页大小，默认为10
     */
    private Integer size = 10;
    
    /**
     * 用户ID，用于查询特定用户的动态
     */
    private Long userId;
    
    /**
     * 可见性筛选：0-公开，1-仅好友，2-仅自己
     */
    private Integer visibility;
    
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
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getVisibility() {
        return visibility;
    }
    
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }
}