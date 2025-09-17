package com.example.nettyim.controller;

import com.example.nettyim.dto.CreateGroupDTO;
import com.example.nettyim.dto.Result;
import com.example.nettyim.entity.Group;
import com.example.nettyim.entity.User;
import com.example.nettyim.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 群组控制器
 */
@RestController
@RequestMapping("/group")
public class GroupController {
    
    private final GroupService groupService;
    
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    
    /**
     * 创建群组
     */
    @PostMapping("/create")
    public Result<Group> createGroup(@RequestParam Long ownerId,
                                    @Valid @RequestBody CreateGroupDTO createGroupDTO) {
        Group group = groupService.createGroup(ownerId, createGroupDTO);
        return Result.success("群组创建成功", group);
    }
    
    /**
     * 加入群组
     */
    @PostMapping("/join")
    public Result<String> joinGroup(@RequestParam Long userId,
                                   @RequestParam Long groupId) {
        groupService.joinGroup(userId, groupId);
        return Result.success("加入群组成功");
    }
    
    /**
     * 邀请用户加入群组
     */
    @PostMapping("/invite")
    public Result<String> inviteToGroup(@RequestParam Long operatorId,
                                       @RequestParam Long groupId,
                                       @RequestBody List<Long> userIds) {
        groupService.inviteToGroup(operatorId, groupId, userIds);
        return Result.success("邀请成功");
    }
    
    /**
     * 退出群组
     */
    @PostMapping("/leave")
    public Result<String> leaveGroup(@RequestParam Long userId,
                                    @RequestParam Long groupId) {
        groupService.leaveGroup(userId, groupId);
        return Result.success("退出群组成功");
    }
    
    /**
     * 踢出群成员
     */
    @PostMapping("/kick")
    public Result<String> kickMember(@RequestParam Long operatorId,
                                    @RequestParam Long groupId,
                                    @RequestParam Long userId) {
        groupService.kickMember(operatorId, groupId, userId);
        return Result.success("踢出成员成功");
    }
    
    /**
     * 设置/取消管理员
     */
    @PostMapping("/admin")
    public Result<String> setAdmin(@RequestParam Long operatorId,
                                  @RequestParam Long groupId,
                                  @RequestParam Long userId,
                                  @RequestParam Boolean isAdmin) {
        groupService.setAdmin(operatorId, groupId, userId, isAdmin);
        String message = isAdmin ? "设置管理员成功" : "取消管理员成功";
        return Result.success(message);
    }
    
    /**
     * 禁言群成员
     */
    @PostMapping("/mute")
    public Result<String> muteMember(@RequestParam Long operatorId,
                                    @RequestParam Long groupId,
                                    @RequestParam Long userId,
                                    @RequestParam Integer muteDuration) {
        groupService.muteMember(operatorId, groupId, userId, muteDuration);
        return Result.success("禁言成功");
    }
    
    /**
     * 解除禁言
     */
    @PostMapping("/unmute")
    public Result<String> unmuteMember(@RequestParam Long operatorId,
                                      @RequestParam Long groupId,
                                      @RequestParam Long userId) {
        groupService.unmuteMember(operatorId, groupId, userId);
        return Result.success("解除禁言成功");
    }
    
    /**
     * 更新群组信息
     */
    @PutMapping("/{groupId}")
    public Result<Group> updateGroup(@PathVariable Long groupId,
                                    @RequestParam Long operatorId,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) String avatar) {
        Group group = groupService.updateGroup(operatorId, groupId, name, description, avatar);
        return Result.success("群组信息更新成功", group);
    }
    
    /**
     * 解散群组
     */
    @DeleteMapping("/{groupId}")
    public Result<String> dissolveGroup(@PathVariable Long groupId,
                                       @RequestParam Long ownerId) {
        groupService.dissolveGroup(ownerId, groupId);
        return Result.success("群组解散成功");
    }
    
    /**
     * 获取群组信息
     */
    @GetMapping("/{groupId}")
    public Result<Group> getGroupInfo(@PathVariable Long groupId) {
        Group group = groupService.getGroupInfo(groupId);
        return Result.success(group);
    }
    
    /**
     * 获取群组成员列表
     */
    @GetMapping("/{groupId}/members")
    public Result<List<User>> getGroupMembers(@PathVariable Long groupId) {
        List<User> members = groupService.getGroupMembers(groupId);
        return Result.success(members);
    }
    
    /**
     * 获取用户加入的群组列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Group>> getUserGroups(@PathVariable Long userId) {
        List<Group> groups = groupService.getUserGroups(userId);
        return Result.success(groups);
    }
    
    /**
     * 检查是否为群成员
     */
    @GetMapping("/{groupId}/member/{userId}")
    public Result<Boolean> isMember(@PathVariable Long groupId,
                                   @PathVariable Long userId) {
        boolean isMember = groupService.isMember(userId, groupId);
        return Result.success(isMember);
    }
}