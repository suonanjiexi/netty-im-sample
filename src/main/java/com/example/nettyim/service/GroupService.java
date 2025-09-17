package com.example.nettyim.service;

import com.example.nettyim.dto.CreateGroupDTO;
import com.example.nettyim.entity.Group;
import com.example.nettyim.entity.GroupMember;
import com.example.nettyim.entity.User;

import java.util.List;

/**
 * 群组服务接口
 */
public interface GroupService {
    
    /**
     * 创建群组
     */
    Group createGroup(Long ownerId, CreateGroupDTO createGroupDTO);
    
    /**
     * 加入群组
     */
    void joinGroup(Long userId, Long groupId);
    
    /**
     * 邀请用户加入群组
     */
    void inviteToGroup(Long operatorId, Long groupId, List<Long> userIds);
    
    /**
     * 退出群组
     */
    void leaveGroup(Long userId, Long groupId);
    
    /**
     * 踢出群成员
     */
    void kickMember(Long operatorId, Long groupId, Long userId);
    
    /**
     * 设置群管理员
     */
    void setAdmin(Long operatorId, Long groupId, Long userId, Boolean isAdmin);
    
    /**
     * 禁言群成员
     */
    void muteMember(Long operatorId, Long groupId, Long userId, Integer muteDuration);
    
    /**
     * 解除禁言
     */
    void unmuteMember(Long operatorId, Long groupId, Long userId);
    
    /**
     * 更新群组信息
     */
    Group updateGroup(Long operatorId, Long groupId, String name, String description, String avatar);
    
    /**
     * 解散群组
     */
    void dissolveGroup(Long ownerId, Long groupId);
    
    /**
     * 获取群组信息
     */
    Group getGroupInfo(Long groupId);
    
    /**
     * 获取群组成员列表
     */
    List<User> getGroupMembers(Long groupId);
    
    /**
     * 获取用户加入的群组列表
     */
    List<Group> getUserGroups(Long userId);
    
    /**
     * 检查用户是否为群成员
     */
    boolean isMember(Long userId, Long groupId);
    
    /**
     * 检查用户是否为群管理员
     */
    boolean isAdmin(Long userId, Long groupId);
    
    /**
     * 检查用户是否为群主
     */
    boolean isOwner(Long userId, Long groupId);
}