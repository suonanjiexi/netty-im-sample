package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.service.MembershipManagementService;
import com.example.nettyim.entity.MembershipLevel;
import com.example.nettyim.entity.UserMembership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员管理控制器
 */
@RestController
@RequestMapping("/admin/api/memberships")
public class MembershipController {
    
    @Autowired
    private MembershipManagementService membershipManagementService;
    
    /**
     * 分页查询会员等级列表
     */
    @GetMapping("/levels")
    public Map<String, Object> pageMembershipLevels(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<MembershipLevel> pageRequest = new Page<>(page, size);
        IPage<MembershipLevel> pageResult = membershipManagementService.pageMembershipLevels(pageRequest);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取会员等级详情
     */
    @GetMapping("/levels/{id}")
    public Map<String, Object> getMembershipLevelById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        MembershipLevel level = membershipManagementService.getMembershipLevelById(id);
        if (level != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", level);
        } else {
            result.put("success", false);
            result.put("message", "会员等级不存在");
        }
        return result;
    }
    
    /**
     * 创建会员等级
     */
    @PostMapping("/levels")
    public Map<String, Object> createMembershipLevel(@RequestBody MembershipLevel membershipLevel) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = membershipManagementService.createMembershipLevel(membershipLevel);
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
     * 更新会员等级
     */
    @PutMapping("/levels/{id}")
    public Map<String, Object> updateMembershipLevel(@PathVariable Long id, @RequestBody MembershipLevel membershipLevel) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = membershipManagementService.updateMembershipLevel(id, membershipLevel);
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
     * 删除会员等级
     */
    @DeleteMapping("/levels/{id}")
    public Map<String, Object> deleteMembershipLevel(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = membershipManagementService.deleteMembershipLevel(id);
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
     * 分页查询用户会员列表
     */
    @GetMapping
    public Map<String, Object> pageUserMemberships(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long levelId,
            @RequestParam(required = false) Integer status) {
        
        Page<UserMembership> pageRequest = new Page<>(page, size);
        IPage<UserMembership> pageResult = membershipManagementService.pageUserMemberships(pageRequest, userId, levelId, status);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取用户会员详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserMembershipById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        UserMembership membership = membershipManagementService.getUserMembershipById(id);
        if (membership != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", membership);
        } else {
            result.put("success", false);
            result.put("message", "用户会员信息不存在");
        }
        return result;
    }
    
    /**
     * 更新用户会员状态
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateUserMembershipStatus(@PathVariable Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = membershipManagementService.updateUserMembershipStatus(id, status);
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
    private Map<String, Object> getPageResultMap(IPage<?> page) {
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