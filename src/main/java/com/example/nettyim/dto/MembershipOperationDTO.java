package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 会员操作DTO
 */
@Data
public class MembershipOperationDTO {
    
    /**
     * 操作类型：UPGRADE-升级，RENEW-续费，TRIAL-试用
     */
    @NotNull(message = "操作类型不能为空")
    private String operationType;
    
    /**
     * 会员等级ID（升级和试用时需要）
     */
    private Long levelId;
    
    /**
     * 续费类型：1-月费，2-年费
     */
    private Integer renewType;
    
    /**
     * 推荐码（新用户注册时使用）
     */
    private String referralCode;
}