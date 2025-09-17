package com.example.nettyim.service.impl;

import com.example.nettyim.dto.IdentityVerifyDTO;
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
        
        // 检查手机号是否已存在
        if (existsByPhone(registerDTO.getPhone())) {
            throw new BusinessException("手机号已存在");
        }
        
        // 检查邮箱是否已存在（如果提供了邮箱）
        if (registerDTO.getEmail() != null && !registerDTO.getEmail().isEmpty() 
            && existsByEmail(registerDTO.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setStatus(1); // 启用状态
        user.setOnlineStatus(0); // 离线状态
        user.setIdentityStatus(0); // 未实名认证
        
        userMapper.insert(user);
        
        // 清除密码字段返回
        user.setPassword(null);
        return user;
    }
    
    @Override
    public String login(UserLoginDTO loginDTO) {
        // 根据账号获取用户（支持用户名和手机号）
        User user = getUserByAccount(loginDTO.getAccount());
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        // 生成JWT Token
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
    
    @Override
    public User getUserByPhone(String phone) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("phone", phone)
                .eq("status", 1));
        
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
    
    @Override
    public User getUserByAccount(String account) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .and(wrapper -> wrapper.eq("username", account).or().eq("phone", account))
                .eq("status", 1));
        
        return user;
    }
    
    @Override
    public boolean existsByPhone(String phone) {
        return userMapper.selectCount(new QueryWrapper<User>()
                .eq("phone", phone)) > 0;
    }
    
    @Override
    public User identityVerify(Long userId, IdentityVerifyDTO verifyDTO) {
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查是否已经实名认证
        if (user.getIdentityStatus() == 1) {
            throw new BusinessException("用户已经实名认证");
        }
        
        // 检查身份证号码是否已被使用
        User existingUser = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id_card_number", verifyDTO.getIdCardNumber())
                .ne("id", userId));
        
        if (existingUser != null) {
            throw new BusinessException("该身份证号码已被使用");
        }
        
        // 更新用户实名认证信息
        user.setRealName(verifyDTO.getRealName());
        user.setIdCardNumber(verifyDTO.getIdCardNumber());
        user.setIdCardFrontUrl(verifyDTO.getIdCardFrontUrl());
        user.setIdCardBackUrl(verifyDTO.getIdCardBackUrl());
        user.setIdentityStatus(1); // 设置为已认证
        user.setIdentityVerifyTime(LocalDateTime.now());
        
        userMapper.updateById(user);
        
        // 清除密码字段返回
        user.setPassword(null);
        return user;
    }
}