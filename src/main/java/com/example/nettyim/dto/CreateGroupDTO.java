package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 创建群组DTO
 */
@Data
public class CreateGroupDTO {
    
    @NotBlank(message = "群组名称不能为空")
    @Size(min = 1, max = 100, message = "群组名称长度必须在1-100之间")
    private String name;
    
    @Size(max = 500, message = "群组描述不能超过500个字符")
    private String description;
    
    private String avatar;
    
    private Integer maxMembers = 500;
    
    private Integer isPublic = 1; // 0-私有，1-公开
    
    /**
     * 初始成员ID列表
     */
    private List<Long> memberIds;
    
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
    
    public Integer getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }
    
    public Integer getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }
    
    public List<Long> getMemberIds() {
        return memberIds;
    }
    
    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }
}