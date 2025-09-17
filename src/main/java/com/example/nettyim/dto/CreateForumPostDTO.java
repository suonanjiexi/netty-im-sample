package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 发帖DTO
 */
@Data
public class CreateForumPostDTO {
    
    @NotNull(message = "贴吧ID不能为空")
    private Long forumId;
    
    @NotBlank(message = "帖子标题不能为空")
    @Size(min = 2, max = 100, message = "帖子标题长度必须在2-100之间")
    private String title;
    
    @NotBlank(message = "帖子内容不能为空")
    @Size(max = 5000, message = "帖子内容不能超过5000字符")
    private String content;
    
    /**
     * 图片URL列表
     */
    private List<String> images;
    
    /**
     * 帖子分类
     */
    private String category;
    
    // Getter and Setter methods
    public Long getForumId() {
        return forumId;
    }
    
    public void setForumId(Long forumId) {
        this.forumId = forumId;
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
    
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
}