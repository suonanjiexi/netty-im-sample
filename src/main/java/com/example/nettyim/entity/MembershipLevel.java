package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员等级实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("membership_levels")
public class MembershipLevel extends BaseEntity {
    
    /**
     * 会员等级ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 会员等级名称
     */
    private String levelName;
    
    /**
     * 会员等级代码
     */
    private String levelCode;
    
    /**
     * 等级排序（数字越大等级越高）
     */
    private Integer levelOrder;
    
    /**
     * 等级描述
     */
    private String description;
    
    /**
     * 等级图标URL
     */
    private String iconUrl;
    
    /**
     * 背景颜色
     */
    private String backgroundColor;
    
    /**
     * 文字颜色
     */
    private String textColor;
    
    /**
     * 月费价格
     */
    private BigDecimal priceMonthly;
    
    /**
     * 年费价格
     */
    private BigDecimal priceYearly;
    
    /**
     * 免费试用天数
     */
    private Integer trialDays;
    
    /**
     * 最大可加入群组数
     */
    private Integer maxGroups;
    
    /**
     * 最大好友数
     */
    private Integer maxFriends;
    
    /**
     * 最大文件上传大小（字节）
     */
    private Long maxFileSize;
    
    /**
     * 最大存储空间（字节）
     */
    private Long maxStorageSize;
    
    /**
     * 是否可以创建群组
     */
    private Boolean canCreateGroup;
    
    /**
     * 是否可以上传文件
     */
    private Boolean canUploadFile;
    
    /**
     * 是否支持视频通话
     */
    private Boolean canVideoCall;
    
    /**
     * 是否支持语音通话
     */
    private Boolean canVoiceCall;
    
    /**
     * 是否支持直播功能
     */
    private Boolean canLiveStream;
    
    /**
     * 是否免广告
     */
    private Boolean adFree;
    
    /**
     * 是否享受优先客服
     */
    private Boolean prioritySupport;
    
    /**
     * 是否支持自定义主题
     */
    private Boolean customTheme;
    
    /**
     * 是否有特殊徽章
     */
    private Boolean specialBadge;
    
    /**
     * 是否激活
     */
    private Boolean isActive;
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Integer getLevelOrder() {
        return levelOrder;
    }

    public void setLevelOrder(Integer levelOrder) {
        this.levelOrder = levelOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public BigDecimal getPriceMonthly() {
        return priceMonthly;
    }

    public void setPriceMonthly(BigDecimal priceMonthly) {
        this.priceMonthly = priceMonthly;
    }

    public BigDecimal getPriceYearly() {
        return priceYearly;
    }

    public void setPriceYearly(BigDecimal priceYearly) {
        this.priceYearly = priceYearly;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public Integer getMaxGroups() {
        return maxGroups;
    }

    public void setMaxGroups(Integer maxGroups) {
        this.maxGroups = maxGroups;
    }

    public Integer getMaxFriends() {
        return maxFriends;
    }

    public void setMaxFriends(Integer maxFriends) {
        this.maxFriends = maxFriends;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public Long getMaxStorageSize() {
        return maxStorageSize;
    }

    public void setMaxStorageSize(Long maxStorageSize) {
        this.maxStorageSize = maxStorageSize;
    }

    public Boolean getCanCreateGroup() {
        return canCreateGroup;
    }

    public void setCanCreateGroup(Boolean canCreateGroup) {
        this.canCreateGroup = canCreateGroup;
    }

    public Boolean getCanUploadFile() {
        return canUploadFile;
    }

    public void setCanUploadFile(Boolean canUploadFile) {
        this.canUploadFile = canUploadFile;
    }

    public Boolean getCanVideoCall() {
        return canVideoCall;
    }

    public void setCanVideoCall(Boolean canVideoCall) {
        this.canVideoCall = canVideoCall;
    }

    public Boolean getCanVoiceCall() {
        return canVoiceCall;
    }

    public void setCanVoiceCall(Boolean canVoiceCall) {
        this.canVoiceCall = canVoiceCall;
    }

    public Boolean getCanLiveStream() {
        return canLiveStream;
    }

    public void setCanLiveStream(Boolean canLiveStream) {
        this.canLiveStream = canLiveStream;
    }

    public Boolean getAdFree() {
        return adFree;
    }

    public void setAdFree(Boolean adFree) {
        this.adFree = adFree;
    }

    public Boolean getPrioritySupport() {
        return prioritySupport;
    }

    public void setPrioritySupport(Boolean prioritySupport) {
        this.prioritySupport = prioritySupport;
    }

    public Boolean getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(Boolean customTheme) {
        this.customTheme = customTheme;
    }

    public Boolean getSpecialBadge() {
        return specialBadge;
    }

    public void setSpecialBadge(Boolean specialBadge) {
        this.specialBadge = specialBadge;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}