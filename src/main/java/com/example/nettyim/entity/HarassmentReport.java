package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 骚扰报告实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("harassment_reports")
public class HarassmentReport extends BaseEntity {
    
    /**
     * 报告ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 举报人ID
     */
    private Long reporterId;
    
    /**
     * 被举报用户ID
     */
    private Long reportedUserId;
    
    /**
     * 报告类型：1-骚扰消息，2-恶意添加，3-垃圾信息，4-其他
     */
    private Integer reportType;
    
    /**
     * 内容类型：1-消息，2-朋友圈，3-贴吧帖子
     */
    private Integer contentType;
    
    /**
     * 内容ID
     */
    private Long contentId;
    
    /**
     * 报告描述
     */
    private String description;
    
    /**
     * 证据截图（JSON数组）
     */
    private String evidenceUrls;
    
    /**
     * 处理状态：0-待处理，1-处理中，2-已处理，3-驳回
     */
    private Integer status;
    
    /**
     * 处理管理员ID
     */
    private Long adminId;
    
    /**
     * 管理员备注
     */
    private String adminNote;
    
    /**
     * 处理时间
     */
    private LocalDateTime processedAt;
}