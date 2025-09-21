package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.User;
import com.example.nettyim.admin.service.UserManagementService;
import com.example.nettyim.admin.dto.UserQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/admin/api/users-management")
public class UserManagementController {
    
    @Autowired
    private UserManagementService userManagementService;
    
    /**
     * 分页查询用户列表
     */
    @GetMapping
    public Map<String, Object> pageUsers(UserQueryDTO queryDTO) {
        Page<User> pageRequest = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        IPage<User> pageResult = userManagementService.pageUsers(pageRequest, queryDTO.getKeyword(), queryDTO.getStatus());
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        User user = userManagementService.getUserByIdWithPassword(id);
        if (user != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", user);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
        }
        return result;
    }
    
    /**
     * 更新用户状态
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = userManagementService.updateStatus(id, status);
        if (success) {
            result.put("success", true);
            result.put("message", "状态更新成功");
        } else {
            result.put("success", false);
            result.put("message", "状态更新失败");
        }
        return result;
    }
    
    /**
     * 构造分页结果
     */
    private Map<String, Object> getPageResultMap(IPage<User> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        return result;
    }
}