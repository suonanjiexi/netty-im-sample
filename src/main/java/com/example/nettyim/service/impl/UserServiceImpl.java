package com.example.nettyim.service.impl;

import com.example.nettyim.dto.UserLoginDTO;
import com.example.nettyim.dto.UserRegisterDTO;
import com.example.nettyim.dto.UserUpdateDTO;
import com.example.nettyim.entity.User;
import com.example.nettyim.exception.BusinessException;
import com.example.nettyim.mapper.UserMapper;
import com.example.nettyim.service.UserService;
import com.example.nettyim.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }
    
    @Override
    public User register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (existsByUsername(registerDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (existsByEmail(registerDTO.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setStatus(1); // 启用状态
        user.setOnlineStatus(0); // 离线状态
        
        userMapper.insert(user);
        
        // 清除密码字段返回
        user.setPassword(null);
        return user;
    }
    
    @Override
    public String login(UserLoginDTO loginDTO) {
        // 获取用户信息
        User user = getUserByUsername(loginDTO.getUsername());
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        // 生成JWT token
        return jwtUtils.generateToken(user.getUsername(), user.getId());
    }
    
    @Override
    public User getUserByUsername(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("username", username)
                .eq("status", 1));
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        return user;
    }
    
    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 清除密码字段
        user.setPassword(null);
        return user;
    }
    
    @Override
    public User updateUser(Long userId, UserUpdateDTO updateDTO) {
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新用户信息
        if (updateDTO.getNickname() != null) {
            user.setNickname(updateDTO.getNickname());
        }
        
        if (updateDTO.getAvatar() != null) {
            user.setAvatar(updateDTO.getAvatar());
        }
        
        if (updateDTO.getOnlineStatus() != null) {
            user.setOnlineStatus(updateDTO.getOnlineStatus());
        }
        
        userMapper.updateById(user);
        
        // 清除密码字段返回
        user.setPassword(null);
        return user;
    }
    
    @Override
    public void updateOnlineStatus(Long userId, Integer onlineStatus) {
        User user = new User();
        user.setId(userId);
        user.setOnlineStatus(onlineStatus);
        userMapper.updateById(user);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userMapper.selectCount(new QueryWrapper<User>()
                .eq("username", username)) > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userMapper.selectCount(new QueryWrapper<User>()
                .eq("email", email)) > 0;
    }
}