package com.example.nettyim.admin.dto;

import lombok.Data;

/**
 * 用户查询DTO
 */
@Data
public class UserQueryDTO {
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
    
    /**
     * 搜索关键词（用户名、昵称、手机号）
     */
    private String keyword;
    
    /**
     * 用户状态：0-禁用，1-启用
     */
    private Integer status;
}