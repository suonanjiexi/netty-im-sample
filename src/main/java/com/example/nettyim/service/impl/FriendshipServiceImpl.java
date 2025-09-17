package com.example.nettyim.service.impl;

import com.example.nettyim.dto.AddFriendDTO;
import com.example.nettyim.entity.Friendship;
import com.example.nettyim.entity.User;
import com.example.nettyim.exception.BusinessException;
import com.example.nettyim.mapper.FriendshipMapper;
import com.example.nettyim.mapper.UserMapper;
import com.example.nettyim.service.FriendshipService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 好友服务实现
 */
@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {
    
    private final FriendshipMapper friendshipMapper;
    private final UserMapper userMapper;
    
    public FriendshipServiceImpl(FriendshipMapper friendshipMapper, UserMapper userMapper) {
        this.friendshipMapper = friendshipMapper;
        this.userMapper = userMapper;
    }
    
    @Override
    public void addFriend(Long userId, AddFriendDTO addFriendDTO) {
        Long friendId = addFriendDTO.getFriendId();
        
        // 检查用户是否存在
        User friend = userMapper.selectById(friendId);
        if (friend == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 不能添加自己为好友
        if (userId.equals(friendId)) {
            throw new BusinessException("不能添加自己为好友");
        }
        
        // 检查是否已经是好友或已发送申请
        Friendship existingFriendship = friendshipMapper.selectOne(
            new QueryWrapper<Friendship>()
                .eq("user_id", userId)
                .eq("friend_id", friendId)
                .in("status", 0, 1) // 0-待确认，1-已同意
        );
        
        if (existingFriendship != null) {
            if (existingFriendship.getStatus() == 0) {
                throw new BusinessException("好友申请已发送，请等待对方确认");
            } else if (existingFriendship.getStatus() == 1) {
                throw new BusinessException("已经是好友关系");
            }
        }
        
        // 创建好友申请
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(0); // 待确认
        friendship.setRemark(addFriendDTO.getRemark());
        
        friendshipMapper.insert(friendship);
    }
    
    @Override
    public void handleFriendRequest(Long userId, Long requestId, Integer action, String remark) {
        // 获取好友申请
        Friendship friendship = friendshipMapper.selectById(requestId);
        if (friendship == null) {
            throw new BusinessException("好友申请不存在");
        }
        
        // 检查是否有权限处理该申请
        if (!friendship.getFriendId().equals(userId)) {
            throw new BusinessException("无权处理该好友申请");
        }
        
        // 检查申请状态
        if (friendship.getStatus() != 0) {
            throw new BusinessException("该申请已被处理");
        }
        
        // 更新申请状态
        friendship.setStatus(action); // 1-同意，2-拒绝
        friendshipMapper.updateById(friendship);
        
        // 如果同意，创建双向好友关系
        if (action == 1) {
            // 创建反向好友关系
            Friendship reverseFriendship = new Friendship();
            reverseFriendship.setUserId(userId);
            reverseFriendship.setFriendId(friendship.getUserId());
            reverseFriendship.setStatus(1); // 已同意
            reverseFriendship.setRemark(remark);
            
            friendshipMapper.insert(reverseFriendship);
        }
    }
    
    @Override
    public List<User> getFriendList(Long userId) {
        // 获取好友ID列表
        List<Friendship> friendships = friendshipMapper.selectList(
            new QueryWrapper<Friendship>()
                .eq("user_id", userId)
                .eq("status", 1) // 已同意
        );
        
        if (friendships.isEmpty()) {
            return List.of();
        }
        
        // 获取好友信息
        List<Long> friendIds = friendships.stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());
        
        List<User> friends = userMapper.selectBatchIds(friendIds);
        
        // 清除密码字段
        friends.forEach(user -> user.setPassword(null));
        
        return friends;
    }
    
    @Override
    public List<Friendship> getFriendRequestList(Long userId) {
        // 获取发送给当前用户的好友申请
        return friendshipMapper.selectList(
            new QueryWrapper<Friendship>()
                .eq("friend_id", userId)
                .eq("status", 0) // 待确认
                .orderByDesc("created_at")
        );
    }
    
    @Override
    public void deleteFriend(Long userId, Long friendId) {
        // 删除双向好友关系
        friendshipMapper.delete(
            new QueryWrapper<Friendship>()
                .eq("user_id", userId)
                .eq("friend_id", friendId)
                .eq("status", 1)
        );
        
        friendshipMapper.delete(
            new QueryWrapper<Friendship>()
                .eq("user_id", friendId)
                .eq("friend_id", userId)
                .eq("status", 1)
        );
    }
    
    @Override
    public void updateFriendRemark(Long userId, Long friendId, String remark) {
        Friendship friendship = friendshipMapper.selectOne(
            new QueryWrapper<Friendship>()
                .eq("user_id", userId)
                .eq("friend_id", friendId)
                .eq("status", 1)
        );
        
        if (friendship == null) {
            throw new BusinessException("好友关系不存在");
        }
        
        friendship.setRemark(remark);
        friendshipMapper.updateById(friendship);
    }
    
    @Override
    public boolean isFriend(Long userId, Long friendId) {
        Long count = friendshipMapper.selectCount(
            new QueryWrapper<Friendship>()
                .eq("user_id", userId)
                .eq("friend_id", friendId)
                .eq("status", 1)
        );
        
        return count > 0;
    }
}