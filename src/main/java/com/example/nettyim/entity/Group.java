package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群组实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("groups")
public class Group extends BaseEntity {
    
    /**
     * 群组ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 群组名称
     */
    private String name;
    
    /**
     * 群组描述
     */
    private String description;
    
    /**
     * 群组头像
     */
    private String avatar;
    
    /**
     * 群主ID
     */
    private Long ownerId;
    
    /**
     * 最大成员数
     */
    private Integer maxMembers;
    
    /**
     * 当前成员数
     */
    private Integer memberCount;
    
    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;
    
    /**
     * 状态：0-禁用，1-启用
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
    
    public Integer getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }
    
    public Integer getMemberCount() {
        return memberCount;
    }
    
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
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