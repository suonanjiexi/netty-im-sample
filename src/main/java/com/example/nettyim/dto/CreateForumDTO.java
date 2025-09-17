package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 创建贴吧DTO
 */
@Data
public class CreateForumDTO {
    
    @NotBlank(message = "贴吧名称不能为空")
    @Size(min = 2, max = 50, message = "贴吧名称长度必须在2-50之间")
    private String name;
    
    @Size(max = 500, message = "贴吧描述不能超过500字符")
    private String description;
    
    /**
     * 贴吧头像URL
     */
    private String avatar;
    
    /**
     * 分类
     */
    private String category;
    
    /**
     * 是否公开：0-私密，1-公开
     */
    private Integer isPublic = 1;
    
    // Getter and Setter methods
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}