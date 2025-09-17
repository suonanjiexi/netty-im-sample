package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 贴吧实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forums")
public class Forum extends BaseEntity {
    
    /**
     * 贴吧ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 贴吧名称
     */
    private String name;
    
    /**
     * 贴吧描述
     */
    private String description;
    
    /**
     * 贴吧头像URL
     */
    private String avatar;
    
    /**
     * 吧主ID
     */
    private Long ownerId;
    
    /**
     * 分类
     */
    private String category;
    
    /**
     * 成员数
     */
    private Integer memberCount;
    
    /**
     * 帖子数
     */
    private Integer postCount;
    
    /**
     * 是否公开：0-私密，1-公开
     */
    private Integer isPublic;
    
    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Long getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getMemberCount() {
        return memberCount;
    }
    
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }
    
    public Integer getPostCount() {
        return postCount;
    }
    
    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }
    
    public Integer getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
}