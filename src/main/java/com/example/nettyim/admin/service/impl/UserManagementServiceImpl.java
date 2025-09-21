package com.example.nettyim.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.admin.service.UserManagementService;
import com.example.nettyim.entity.User;
import com.example.nettyim.mapper.UserMapper;
import com.example.nettyim.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务实现类
 */
@Service
public class UserManagementServiceImpl extends UserServiceImpl implements UserManagementService {
    
    private final UserMapper userMapper;
    
    public UserManagementServiceImpl(UserMapper userMapper) {
        super(userMapper, null, null);
        this.userMapper = userMapper;
    }
    
    @Override
    public IPage<User> pageUsers(Page<User> page, String keyword, Integer status) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                    .like("username", keyword)
                    .or()
                    .like("nickname", keyword)
                    .or()
                    .like("phone", keyword));
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("created_at");
        return userMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public User getUserByIdWithPassword(Long userId) {
        return userMapper.selectById(userId);
    }
    
    @Override
    public boolean updateStatus(Long userId, Integer status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        return userMapper.updateById(user) > 0;
    }
}