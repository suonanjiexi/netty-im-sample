package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 发布朋友圈动态DTO
 */
@Data
public class PublishMomentDTO {
    
    @NotBlank(message = "动态内容不能为空")
    @Size(max = 1000, message = "动态内容不能超过1000字符")
    private String content;
    
    /**
     * 图片URL列表
     */
    private List<String> images;
    
    /**
     * 位置信息
     */
    private String location;
    
    /**
     * 可见性：0-公开，1-仅好友，2-仅自己
     */
    private Integer visibility = 0;
    
    // Getter and Setter methods
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
}