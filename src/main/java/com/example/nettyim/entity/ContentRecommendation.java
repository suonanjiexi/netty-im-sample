package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 内容推荐实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("content_recommendations")
public class ContentRecommendation extends BaseEntity {
    
    /**
     * 推荐ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 内容类型：1-朋友圈，2-贴吧帖子，3-群组，4-用户
     */
    private Integer contentType;
    
    /**
     * 内容ID
     */
    private Long contentId;
    
    /**
     * 推荐分数
     */
    private BigDecimal score;
    
    /**
     * 推荐算法
     */
    private String algorithm;
    
    /**
     * 推荐原因
     */
    private String reason;
    
    /**
     * 是否已展示
     */
    private Boolean isShown;
    
    /**
     * 是否已点击
     */
    private Boolean isClicked;
    
    /**
     * 展示时间
     */
    private LocalDateTime shownAt;
    
    /**
     * 点击时间
     */
    private LocalDateTime clickedAt;
}