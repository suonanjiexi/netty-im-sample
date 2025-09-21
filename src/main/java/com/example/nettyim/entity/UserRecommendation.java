package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户推荐实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_recommendations")
public class UserRecommendation extends BaseEntity {
    
    /**
     * 推荐ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 被推荐用户ID
     */
    private Long userId;
    
    /**
     * 推荐的用户ID
     */
    private Long recommendedUserId;
    
    /**
     * 推荐类型：1-通讯录，2-位置，3-兴趣，4-共同好友，5-群组
     */
    private Integer recommendationType;
    
    /**
     * 推荐分数
     */
    private BigDecimal score;
    
    /**
     * 推荐原因
     */
    private String reason;
    
    /**
     * 状态：0-待处理，1-已接受，2-已拒绝，3-已忽略
     */
    private Integer status;
}