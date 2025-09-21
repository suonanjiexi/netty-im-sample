package com.example.nettyim.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.admin.entity.AdminUser;
import com.example.nettyim.admin.mapper.AdminUserMapper;
import com.example.nettyim.admin.service.AdminUserService;
import com.example.nettyim.admin.dto.AdminLoginDTO;
import com.example.nettyim.admin.dto.AdminUserDTO;
import com.example.nettyim.utils.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 管理员用户服务实现类
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {
    
    @Override
    public AdminUser login(AdminLoginDTO loginDTO) {
        // 根据用户名查找管理员
        AdminUser adminUser = this.baseMapper.findByUsername(loginDTO.getUsername());
        if (adminUser == null) {
            return null;
        }
        
        // 验证密码
        if (!PasswordUtil.verifyPassword(loginDTO.getPassword(), adminUser.getPassword())) {
            return null;
        }
        
        // 更新最后登录时间
        adminUser.setLastLoginTime(LocalDateTime.now());
        this.updateById(adminUser);
        
        return adminUser;
    }
    
    @Override
    public AdminUser getById(Long id) {
        return this.baseMapper.selectById(id);
    }
    
    @Override
    public IPage<AdminUser> pageAdminUsers(Page<AdminUser> page, String keyword) {
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("username", keyword).or().like("nickname", keyword);
        }
        queryWrapper.orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
    
    @Override
    public boolean createAdminUser(AdminUserDTO adminUserDTO) {
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(adminUserDTO, adminUser);
        
        // 密码加密
        if (adminUserDTO.getPassword() != null && !adminUserDTO.getPassword().isEmpty()) {
            adminUser.setPassword(PasswordUtil.encryptPassword(adminUserDTO.getPassword()));
        }
        
        return this.save(adminUser);
    }
    
    @Override
    public boolean updateAdminUser(Long id, AdminUserDTO adminUserDTO) {
        AdminUser adminUser = this.getById(id);
        if (adminUser == null) {
            return false;
        }
        
        // 如果密码不为空，则进行加密
        if (adminUserDTO.getPassword() != null && !adminUserDTO.getPassword().isEmpty()) {
            adminUserDTO.setPassword(PasswordUtil.encryptPassword(adminUserDTO.getPassword()));
        } else {
            // 如果密码为空，则不更新密码字段
            adminUserDTO.setPassword(null);
        }
        
        BeanUtils.copyProperties(adminUserDTO, adminUser);
        return this.updateById(adminUser);
    }
    
    @Override
    public boolean deleteAdminUser(Long id) {
        return this.removeById(id);
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        AdminUser adminUser = this.getById(id);
        if (adminUser == null) {
            return false;
        }
        
        adminUser.setStatus(status);
        return this.updateById(adminUser);
    }
}