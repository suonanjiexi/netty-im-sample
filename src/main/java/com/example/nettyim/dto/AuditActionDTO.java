package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 审核操作DTO
 */
@Data
public class AuditActionDTO {
    
    /**
     * 审核状态：1-审核通过，2-审核拒绝
     */
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;
    
    /**
     * 审核结果说明
     */
    private String auditResult;
}