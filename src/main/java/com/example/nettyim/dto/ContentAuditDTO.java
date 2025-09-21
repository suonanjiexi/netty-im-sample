package com.example.nettyim.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内容审核DTO
 */
@Data
public class ContentAuditDTO {
    
    /**
     * 审核记录ID
     */
    private Long id;
    
    /**
     * 内容类型：1-消息，2-朋友圈动态，3-朋友圈评论，4-贴吧帖子，5-贴吧回复，6-用户资料
     */
    private Integer contentType;
    
    /**
     * 内容ID
     */
    private Long contentId;
    
    /**
     * 内容发布者ID
     */
    private Long userId;
    
    /**
     * 审核内容
     */
    private String content;
    
    /**
     * 审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝
     */
    private Integer auditStatus;
    
    /**
     * 审核结果
     */
    private String auditResult;
    
    /**
     * 审核员ID
     */
    private Long auditorId;
    
    /**
     * 审核方式：0-自动审核，1-人工审核
     */
    private Integer auditType;
    
    /**
     * 敏感词匹配结果
     */
    private String sensitiveWords;
    
    /**
     * 审核分数
     */
    private Integer auditScore;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    
    /**
     * 是否需要人工复审
     */
    private Integer needManualReview;
    
    /**
     * 用户昵称
     */
    private String userNickname;
    
    /**
     * 审核员昵称
     */
    private String auditorNickname;
}