package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.entity.AdminUser;
import com.example.nettyim.admin.service.AdminUserService;
import com.example.nettyim.admin.dto.AdminLoginDTO;
import com.example.nettyim.admin.dto.AdminLoginResponseDTO;
import com.example.nettyim.admin.dto.AdminUserDTO;
import com.example.nettyim.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员用户控制器
 */
@RestController
@RequestMapping("/admin/api/users")
public class AdminUserController {
    
    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody AdminLoginDTO loginDTO) {
        Map<String, Object> result = new HashMap<>();
        
        AdminUser adminUser = adminUserService.login(loginDTO);
        if (adminUser == null) {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            return result;
        }
        
        // 生成JWT token
        String token = jwtUtils.generateToken(adminUser.getUsername(), adminUser.getId());
        
        // 构造响应数据
        AdminLoginResponseDTO responseDTO = new AdminLoginResponseDTO();
        responseDTO.setId(adminUser.getId());
        responseDTO.setUsername(adminUser.getUsername());
        responseDTO.setNickname(adminUser.getNickname());
        responseDTO.setAvatar(adminUser.getAvatar());
        responseDTO.setRole(adminUser.getRole());
        responseDTO.setToken(token);
        
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("data", responseDTO);
        return result;
    }
    
    /**
     * 分页查询管理员列表
     */
    @GetMapping
    public Map<String, Object> pageAdminUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        
        Page<AdminUser> pageRequest = new Page<>(page, size);
        return getPageResultMap(adminUserService.pageAdminUsers(pageRequest, keyword));
    }
    
    /**
     * 创建管理员
     */
    @PostMapping
    public Map<String, Object> createAdminUser(@Valid @RequestBody AdminUserDTO adminUserDTO) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = adminUserService.createAdminUser(adminUserDTO);
        if (success) {
            result.put("success", true);
            result.put("message", "创建成功");
        } else {
            result.put("success", false);
            result.put("message", "创建失败");
        }
        return result;
    }
    
    /**
     * 更新管理员
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateAdminUser(@PathVariable Long id, @Valid @RequestBody AdminUserDTO adminUserDTO) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = adminUserService.updateAdminUser(id, adminUserDTO);
        if (success) {
            result.put("success", true);
            result.put("message", "更新成功");
        } else {
            result.put("success", false);
            result.put("message", "更新失败");
        }
        return result;
    }
    
    /**
     * 删除管理员
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteAdminUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = adminUserService.deleteAdminUser(id);
        if (success) {
            result.put("success", true);
            result.put("message", "删除成功");
        } else {
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }
    
    /**
     * 更新管理员状态
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = adminUserService.updateStatus(id, status);
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
    private Map<String, Object> getPageResultMap(IPage<AdminUser> page) {
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