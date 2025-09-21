package com.example.nettyim.service;

import com.example.nettyim.entity.UserRecommendation;
import com.example.nettyim.entity.UserInterest;
import com.example.nettyim.entity.UserLocation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 推荐服务接口
 */
public interface RecommendationService {
    
    /**
     * 获取好友推荐列表
     */
    List<UserRecommendation> getFriendRecommendations(Long userId, Integer limit);
    
    /**
     * 基于通讯录的好友推荐
     */
    List<UserRecommendation> getContactBasedRecommendations(Long userId, List<String> phoneNumbers);
    
    /**
     * 基于位置的好友推荐
     */
    List<UserRecommendation> getLocationBasedRecommendations(Long userId, Double radiusKm);
    
    /**
     * 基于兴趣的好友推荐
     */
    List<UserRecommendation> getInterestBasedRecommendations(Long userId);
    
    /**
     * 基于共同好友的推荐
     */
    List<UserRecommendation> getMutualFriendsRecommendations(Long userId);
    
    /**
     * 基于群组的好友推荐
     */
    List<UserRecommendation> getGroupBasedRecommendations(Long userId);
    
    /**
     * 更新用户兴趣标签
     */
    Boolean updateUserInterests(Long userId, List<UserInterest> interests);
    
    /**
     * 更新用户位置信息
     */
    Boolean updateUserLocation(Long userId, UserLocation location);
    
    /**
     * 处理推荐反馈（接受/拒绝/忽略）
     */
    Boolean handleRecommendationFeedback(Long recommendationId, Integer status);
    
    /**
     * 计算用户相似度
     */
    BigDecimal calculateUserSimilarity(Long userId1, Long userId2);
    
    /**
     * 刷新用户推荐
     */
    void refreshUserRecommendations(Long userId);
    
    /**
     * 获取用户兴趣标签
     */
    List<UserInterest> getUserInterests(Long userId);
    
    /**
     * 获取推荐统计信息
     */
    Map<String, Object> getRecommendationStats(Long userId);
}