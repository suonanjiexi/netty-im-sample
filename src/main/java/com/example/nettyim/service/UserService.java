package com.example.nettyim.service;

import com.example.nettyim.dto.IdentityVerifyDTO;
import com.example.nettyim.dto.UserLoginDTO;
import com.example.nettyim.dto.UserRegisterDTO;
import com.example.nettyim.dto.UserUpdateDTO;
import com.example.nettyim.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    User register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     */
    String login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);
    
    /**
     * 根据手机号获取用户
     */
    User getUserByPhone(String phone);
    
    /**
     * 根据账号获取用户（支持用户名和手机号）
     */
    User getUserByAccount(String account);
    
    /**
     * 根据用户ID获取用户
     */
    User getUserById(Long userId);
    
    /**
     * 更新用户信息
     */
    User updateUser(Long userId, UserUpdateDTO updateDTO);
    
    /**
     * 更新用户在线状态
     */
    void updateOnlineStatus(Long userId, Integer onlineStatus);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);
    
    /**
     * 身份证实名认证
     */
    User identityVerify(Long userId, IdentityVerifyDTO verifyDTO);
}