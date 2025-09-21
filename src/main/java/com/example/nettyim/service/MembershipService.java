package com.example.nettyim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.MembershipLevel;
import com.example.nettyim.entity.UserMembership;

import java.math.BigDecimal;
import java.util.List;

/**
 * 会员服务接口
 */
public interface MembershipService {
    
    /**
     * 获取所有活跃的会员等级
     */
    List<MembershipLevel> getAllActiveLevels();
    
    /**
     * 根据代码获取会员等级
     */
    MembershipLevel getLevelByCode(String levelCode);
    
    /**
     * 根据ID获取会员等级
     */
    MembershipLevel getLevelById(Long levelId);
    
    /**
     * 获取用户会员信息
     */
    UserMembership getUserMembership(Long userId);
    
    /**
     * 创建用户会员记录（新用户注册时）
     */
    Boolean createUserMembership(Long userId);
    
    /**
     * 升级用户会员等级
     */
    Boolean upgradeMembership(Long userId, Long levelId, Integer renewType, String orderNo);
    
    /**
     * 续费会员
     */
    Boolean renewMembership(Long userId, Integer renewType, String orderNo);
    
    /**
     * 激活试用会员
     */
    Boolean activateTrialMembership(Long userId, Long levelId);
    
    /**
     * 检查并更新过期会员状态
     */
    void checkAndUpdateExpiredMemberships();
    
    /**
     * 获取用户当前积分
     */
    Integer getUserPoints(Long userId);
    
    /**
     * 增加用户积分
     */
    Boolean addUserPoints(Long userId, Integer points, String sourceType, Long sourceId, String description);
    
    /**
     * 扣除用户积分
     */
    Boolean deductUserPoints(Long userId, Integer points, String sourceType, Long sourceId, String description);
    
    /**
     * 获取用户积分记录
     */
    IPage<com.example.nettyim.entity.MembershipPoint> getUserPointsHistory(Long userId, Page<com.example.nettyim.entity.MembershipPoint> page);
    
    /**
     * 生成推荐码
     */
    String generateReferralCode(Long userId);
    
    /**
     * 处理推荐奖励
     */
    Boolean processReferralReward(Long referrerUserId, Long newUserId);
    
    /**
     * 检查用户是否有权限
     */
    Boolean hasPermission(Long userId, String permission);
    
    /**
     * 获取用户会员权益
     */
    List<String> getUserBenefits(Long userId);
}