package com.example.nettyim.service;

import com.example.nettyim.dto.AddFriendDTO;
import com.example.nettyim.entity.Friendship;
import com.example.nettyim.entity.User;

import java.util.List;

/**
 * 好友服务接口
 */
public interface FriendshipService {
    
    /**
     * 添加好友（发送好友申请）
     */
    void addFriend(Long userId, AddFriendDTO addFriendDTO);
    
    /**
     * 处理好友申请
     */
    void handleFriendRequest(Long userId, Long requestId, Integer action, String remark);
    
    /**
     * 获取好友列表
     */
    List<User> getFriendList(Long userId);
    
    /**
     * 获取好友申请列表
     */
    List<Friendship> getFriendRequestList(Long userId);
    
    /**
     * 删除好友
     */
    void deleteFriend(Long userId, Long friendId);
    
    /**
     * 更新好友备注
     */
    void updateFriendRemark(Long userId, Long friendId, String remark);
    
    /**
     * 检查是否为好友关系
     */
    boolean isFriend(Long userId, Long friendId);
}