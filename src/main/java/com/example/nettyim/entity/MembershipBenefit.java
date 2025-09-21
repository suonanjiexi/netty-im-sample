package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员权益实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("membership_benefits")
public class MembershipBenefit extends BaseEntity {
    
    /**
     * 权益ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 会员等级ID
     */
    private Long levelId;
    
    /**
     * 权益类型
     */
    private String benefitType;
    
    /**
     * 权益名称
     */
    private String benefitName;
    
    /**
     * 权益描述
     */
    private String benefitDescription;
    
    /**
     * 权益值
     */
    private String benefitValue;
    
    /**
     * 是否激活
     */
    private Boolean isActive;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
}