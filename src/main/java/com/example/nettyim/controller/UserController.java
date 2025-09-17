package com.example.nettyim.controller;

import com.example.nettyim.dto.Result;
import com.example.nettyim.dto.UserLoginDTO;
import com.example.nettyim.dto.UserRegisterDTO;
import com.example.nettyim.dto.UserUpdateDTO;
import com.example.nettyim.entity.User;
import com.example.nettyim.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        return Result.success("注册成功", user);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        User user = userService.getUserByUsername(loginDTO.getUsername());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        
        return Result.success("登录成功", data);
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    public Result<User> getUserInfo(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return Result.success(user);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    public Result<User> updateUser(@PathVariable Long userId, 
                                   @Valid @RequestBody UserUpdateDTO updateDTO) {
        User user = userService.updateUser(userId, updateDTO);
        return Result.success("更新成功", user);
    }
    
    /**
     * 更新在线状态
     */
    @PutMapping("/{userId}/status")
    public Result<String> updateOnlineStatus(@PathVariable Long userId, 
                                          @RequestParam Integer status) {
        userService.updateOnlineStatus(userId, status);
        return Result.success("状态更新成功");
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check/email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return Result.success(exists);
    }
}