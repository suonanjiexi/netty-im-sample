package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.User;
import com.example.nettyim.service.UserService;

/**
 * 用户管理服务接口（扩展）
 */
public interface UserManagementService extends UserService {
    
    /**
     * 分页查询用户列表
     * @param page 分页参数
     * @param keyword 搜索关键词
     * @param status 用户状态
     * @return 用户分页列表
     */
    IPage<User> pageUsers(Page<User> page, String keyword, Integer status);
    
    /**
     * 根据ID获取用户（包含密码字段）
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserByIdWithPassword(Long userId);
    
    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-启用
     * @return 是否成功
     */
    boolean updateStatus(Long userId, Integer status);
}