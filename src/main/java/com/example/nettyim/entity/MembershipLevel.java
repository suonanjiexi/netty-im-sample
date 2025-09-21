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
}