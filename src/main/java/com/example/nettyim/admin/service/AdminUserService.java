package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.entity.AdminUser;
import com.example.nettyim.admin.dto.AdminLoginDTO;
import com.example.nettyim.admin.dto.AdminUserDTO;

/**
 * 管理员用户服务接口
 */
public interface AdminUserService {
    
    /**
     * 管理员登录
     * @param loginDTO 登录信息
     * @return 管理员信息
     */
    AdminUser login(AdminLoginDTO loginDTO);
    
    /**
     * 根据ID获取管理员
     * @param id 管理员ID
     * @return 管理员信息
     */
    AdminUser getById(Long id);
    
    /**
     * 分页查询管理员列表
     * @param page 分页参数
     * @param keyword 搜索关键词
     * @return 管理员分页列表
     */
    IPage<AdminUser> pageAdminUsers(Page<AdminUser> page, String keyword);
    
    /**
     * 创建管理员
     * @param adminUserDTO 管理员信息
     * @return 是否成功
     */
    boolean createAdminUser(AdminUserDTO adminUserDTO);
    
    /**
     * 更新管理员
     * @param id 管理员ID
     * @param adminUserDTO 管理员信息
     * @return 是否成功
     */
    boolean updateAdminUser(Long id, AdminUserDTO adminUserDTO);
    
    /**
     * 删除管理员
     * @param id 管理员ID
     * @return 是否成功
     */
    boolean deleteAdminUser(Long id);
    
    /**
     * 更新管理员状态
     * @param id 管理员ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);
}