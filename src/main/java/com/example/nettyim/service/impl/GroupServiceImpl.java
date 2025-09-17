package com.example.nettyim.service.impl;

import com.example.nettyim.dto.CreateGroupDTO;
import com.example.nettyim.entity.Group;
import com.example.nettyim.entity.GroupMember;
import com.example.nettyim.entity.User;
import com.example.nettyim.exception.BusinessException;
import com.example.nettyim.mapper.GroupMapper;
import com.example.nettyim.mapper.GroupMemberMapper;
import com.example.nettyim.mapper.UserMapper;
import com.example.nettyim.service.GroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群组服务实现
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    
    private final GroupMapper groupMapper;
    private final GroupMemberMapper groupMemberMapper;
    private final UserMapper userMapper;
    
    public GroupServiceImpl(GroupMapper groupMapper, GroupMemberMapper groupMemberMapper, UserMapper userMapper) {
        this.groupMapper = groupMapper;
        this.groupMemberMapper = groupMemberMapper;
        this.userMapper = userMapper;
    }
    
    @Override
    public Group createGroup(Long ownerId, CreateGroupDTO createGroupDTO) {
        // 创建群组
        Group group = new Group();
        group.setName(createGroupDTO.getName());
        group.setDescription(createGroupDTO.getDescription());
        group.setAvatar(createGroupDTO.getAvatar());
        group.setOwnerId(ownerId);
        group.setMaxMembers(createGroupDTO.getMaxMembers());
        group.setMemberCount(1); // 群主
        group.setIsPublic(createGroupDTO.getIsPublic());
        group.setStatus(1);
        
        groupMapper.insert(group);
        
        // 添加群主为成员
        GroupMember ownerMember = new GroupMember();
        ownerMember.setGroupId(group.getId());
        ownerMember.setUserId(ownerId);
        ownerMember.setRole(2); // 群主
        ownerMember.setJoinTime(LocalDateTime.now());
        ownerMember.setStatus(1);
        
        groupMemberMapper.insert(ownerMember);
        
        // 添加初始成员
        if (createGroupDTO.getMemberIds() != null && !createGroupDTO.getMemberIds().isEmpty()) {
            for (Long memberId : createGroupDTO.getMemberIds()) {
                if (!memberId.equals(ownerId)) {
                    inviteToGroup(ownerId, group.getId(), List.of(memberId));
                }
            }
        }
        
        return group;
    }
    
    @Override
    public void joinGroup(Long userId, Long groupId) {
        Group group = getGroupInfo(groupId);
        
        // 检查群组是否存在且启用
        if (group == null || group.getStatus() != 1) {
            throw new BusinessException("群组不存在或已禁用");
        }
        
        // 检查是否已经是成员
        if (isMember(userId, groupId)) {
            throw new BusinessException("已经是群成员");
        }
        
        // 检查群组是否已满
        if (group.getMemberCount() >= group.getMaxMembers()) {
            throw new BusinessException("群组人数已满");
        }
        
        // 添加成员
        GroupMember member = new GroupMember();
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setRole(0); // 普通成员
        member.setJoinTime(LocalDateTime.now());
        member.setStatus(1);
        
        groupMemberMapper.insert(member);
        
        // 更新群组成员数
        group.setMemberCount(group.getMemberCount() + 1);
        groupMapper.updateById(group);
    }
    
    @Override
    public void inviteToGroup(Long operatorId, Long groupId, List<Long> userIds) {
        // 检查操作权限
        if (!isAdmin(operatorId, groupId) && !isOwner(operatorId, groupId)) {
            throw new BusinessException("无权限邀请成员");
        }
        
        Group group = getGroupInfo(groupId);
        
        for (Long userId : userIds) {
            // 检查用户是否存在
            User user = userMapper.selectById(userId);
            if (user == null) {
                continue; // 跳过不存在的用户
            }
            
            // 检查是否已经是成员
            if (isMember(userId, groupId)) {
                continue; // 跳过已经是成员的用户
            }
            
            // 检查群组是否已满
            if (group.getMemberCount() >= group.getMaxMembers()) {
                break; // 群已满，停止邀请
            }
            
            // 添加成员
            GroupMember member = new GroupMember();
            member.setGroupId(groupId);
            member.setUserId(userId);
            member.setRole(0); // 普通成员
            member.setJoinTime(LocalDateTime.now());
            member.setStatus(1);
            
            groupMemberMapper.insert(member);
            
            // 更新群组成员数
            group.setMemberCount(group.getMemberCount() + 1);
        }
        
        groupMapper.updateById(group);
    }
    
    @Override
    public void leaveGroup(Long userId, Long groupId) {
        // 群主不能直接退出，需要先转让群主或解散群
        if (isOwner(userId, groupId)) {
            throw new BusinessException("群主不能退出群组，请先转让群主权限或解散群组");
        }
        
        // 删除成员记录
        groupMemberMapper.delete(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("user_id", userId)
        );
        
        // 更新群组成员数
        Group group = getGroupInfo(groupId);
        group.setMemberCount(group.getMemberCount() - 1);
        groupMapper.updateById(group);
    }
    
    @Override
    public void kickMember(Long operatorId, Long groupId, Long userId) {
        // 检查操作权限
        if (!isAdmin(operatorId, groupId) && !isOwner(operatorId, groupId)) {
            throw new BusinessException("无权限踢出成员");
        }
        
        // 群主不能被踢出
        if (isOwner(userId, groupId)) {
            throw new BusinessException("不能踢出群主");
        }
        
        // 管理员只能被群主踢出
        if (isAdmin(userId, groupId) && !isOwner(operatorId, groupId)) {
            throw new BusinessException("管理员只能被群主踢出");
        }
        
        leaveGroup(userId, groupId);
    }
    
    @Override
    public void setAdmin(Long operatorId, Long groupId, Long userId, Boolean isAdmin) {
        // 只有群主可以设置管理员
        if (!isOwner(operatorId, groupId)) {
            throw new BusinessException("只有群主可以设置管理员");
        }
        
        GroupMember member = groupMemberMapper.selectOne(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("user_id", userId)
                .eq("status", 1)
        );
        
        if (member == null) {
            throw new BusinessException("用户不是群成员");
        }
        
        // 更新角色
        member.setRole(isAdmin ? 1 : 0); // 1-管理员，0-普通成员
        groupMemberMapper.updateById(member);
    }
    
    @Override
    public void muteMember(Long operatorId, Long groupId, Long userId, Integer muteDuration) {
        // 检查操作权限
        if (!isAdmin(operatorId, groupId) && !isOwner(operatorId, groupId)) {
            throw new BusinessException("无权限禁言成员");
        }
        
        // 群主和管理员不能被禁言
        if (isOwner(userId, groupId) || (isAdmin(userId, groupId) && !isOwner(operatorId, groupId))) {
            throw new BusinessException("无权限禁言该成员");
        }
        
        GroupMember member = groupMemberMapper.selectOne(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("user_id", userId)
                .eq("status", 1)
        );
        
        if (member == null) {
            throw new BusinessException("用户不是群成员");
        }
        
        // 设置禁言到期时间
        member.setMuteUntil(LocalDateTime.now().plusMinutes(muteDuration));
        groupMemberMapper.updateById(member);
    }
    
    @Override
    public void unmuteMember(Long operatorId, Long groupId, Long userId) {
        // 检查操作权限
        if (!isAdmin(operatorId, groupId) && !isOwner(operatorId, groupId)) {
            throw new BusinessException("无权限解除禁言");
        }
        
        GroupMember member = groupMemberMapper.selectOne(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("user_id", userId)
                .eq("status", 1)
        );
        
        if (member == null) {
            throw new BusinessException("用户不是群成员");
        }
        
        // 解除禁言
        member.setMuteUntil(null);
        groupMemberMapper.updateById(member);
    }
    
    @Override
    public Group updateGroup(Long operatorId, Long groupId, String name, String description, String avatar) {
        // 检查操作权限
        if (!isAdmin(operatorId, groupId) && !isOwner(operatorId, groupId)) {
            throw new BusinessException("无权限修改群组信息");
        }
        
        Group group = getGroupInfo(groupId);
        
        if (name != null) {
            group.setName(name);
        }
        if (description != null) {
            group.setDescription(description);
        }
        if (avatar != null) {
            group.setAvatar(avatar);
        }
        
        groupMapper.updateById(group);
        return group;
    }
    
    @Override
    public void dissolveGroup(Long ownerId, Long groupId) {
        // 只有群主可以解散群组
        if (!isOwner(ownerId, groupId)) {
            throw new BusinessException("只有群主可以解散群组");
        }
        
        // 删除所有成员
        groupMemberMapper.delete(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
        );
        
        // 删除群组
        groupMapper.deleteById(groupId);
    }
    
    @Override
    public Group getGroupInfo(Long groupId) {
        return groupMapper.selectById(groupId);
    }
    
    @Override
    public List<User> getGroupMembers(Long groupId) {
        // 获取群成员ID列表
        List<GroupMember> members = groupMemberMapper.selectList(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("status", 1)
                .orderByDesc("role") // 群主和管理员排在前面
                .orderByAsc("join_time")
        );
        
        if (members.isEmpty()) {
            return List.of();
        }
        
        // 获取用户信息
        List<Long> userIds = members.stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toList());
        
        List<User> users = userMapper.selectBatchIds(userIds);
        
        // 清除密码字段
        users.forEach(user -> user.setPassword(null));
        
        return users;
    }
    
    @Override
    public List<Group> getUserGroups(Long userId) {
        // 获取用户加入的群组ID列表
        List<GroupMember> memberships = groupMemberMapper.selectList(
            new QueryWrapper<GroupMember>()
                .eq("user_id", userId)
                .eq("status", 1)
        );
        
        if (memberships.isEmpty()) {
            return List.of();
        }
        
        // 获取群组信息
        List<Long> groupIds = memberships.stream()
                .map(GroupMember::getGroupId)
                .collect(Collectors.toList());
        
        return groupMapper.selectBatchIds(groupIds);
    }
    
    @Override
    public boolean isMember(Long userId, Long groupId) {
        Long count = groupMemberMapper.selectCount(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("user_id", userId)
                .eq("status", 1)
        );
        
        return count > 0;
    }
    
    @Override
    public boolean isAdmin(Long userId, Long groupId) {
        Long count = groupMemberMapper.selectCount(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("user_id", userId)
                .eq("role", 1) // 管理员
                .eq("status", 1)
        );
        
        return count > 0;
    }
    
    @Override
    public boolean isOwner(Long userId, Long groupId) {
        Long count = groupMemberMapper.selectCount(
            new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("user_id", userId)
                .eq("role", 2) // 群主
                .eq("status", 1)
        );
        
        return count > 0;
    }
}