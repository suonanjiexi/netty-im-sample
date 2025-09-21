package com.example.nettyim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.*;
import com.example.nettyim.mapper.*;
import com.example.nettyim.service.MembershipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员服务实现类
 */
@Slf4j
@Service
public class MembershipServiceImpl implements MembershipService {
    
    @Autowired
    private MembershipLevelMapper membershipLevelMapper;
    
    @Autowired
    private UserMembershipMapper userMembershipMapper;
    
    @Autowired
    private MembershipBenefitMapper membershipBenefitMapper;
    
    @Autowired
    private MembershipPointMapper membershipPointMapper;
    
    @Override
    public List<MembershipLevel> getAllActiveLevels() {
        LambdaQueryWrapper<MembershipLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MembershipLevel::getIsActive, true)
                .orderByAsc(MembershipLevel::getLevelOrder);
        return membershipLevelMapper.selectList(wrapper);
    }
    
    @Override
    public MembershipLevel getLevelByCode(String levelCode) {
        LambdaQueryWrapper<MembershipLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MembershipLevel::getLevelCode, levelCode)
                .eq(MembershipLevel::getIsActive, true);
        return membershipLevelMapper.selectOne(wrapper);
    }
    
    @Override
    public MembershipLevel getLevelById(Long levelId) {
        return membershipLevelMapper.selectById(levelId);
    }
    
    @Override
    public UserMembership getUserMembership(Long userId) {
        LambdaQueryWrapper<UserMembership> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMembership::getUserId, userId);
        return userMembershipMapper.selectOne(wrapper);
    }
    
    @Override
    @Transactional
    public Boolean createUserMembership(Long userId) {
        // 检查是否已存在会员记录
        UserMembership existing = getUserMembership(userId);
        if (existing != null) {
            return true;
        }
        
        // 获取免费会员等级
        MembershipLevel freeLevel = getLevelByCode("FREE");
        if (freeLevel == null) {
            log.error("找不到免费会员等级");
            return false;
        }
        
        // 创建会员记录
        UserMembership membership = new UserMembership();
        membership.setUserId(userId);
        membership.setLevelId(freeLevel.getId());
        membership.setStatus(1);
        membership.setStartTime(LocalDateTime.now());
        membership.setIsTrial(false);
        membership.setAutoRenew(false);
        membership.setRenewType(1);
        membership.setTotalPaid(BigDecimal.ZERO);
        membership.setPointsEarned(0);
        membership.setPointsUsed(0);
        membership.setReferralCode(generateReferralCode(userId));
        membership.setCreatedAt(LocalDateTime.now());
        membership.setUpdatedAt(LocalDateTime.now());
        
        return userMembershipMapper.insert(membership) > 0;
    }
    
    @Override
    @Transactional
    public Boolean upgradeMembership(Long userId, Long levelId, Integer renewType, String orderNo) {
        UserMembership membership = getUserMembership(userId);
        if (membership == null) {
            log.error("用户会员记录不存在: {}", userId);
            return false;
        }
        
        MembershipLevel newLevel = getLevelById(levelId);
        if (newLevel == null) {
            log.error("会员等级不存在: {}", levelId);
            return false;
        }
        
        // 计算结束时间
        LocalDateTime endTime = null;
        if (renewType == 1) {
            // 月费
            endTime = LocalDateTime.now().plusMonths(1);
        } else if (renewType == 2) {
            // 年费
            endTime = LocalDateTime.now().plusYears(1);
        }
        
        // 更新会员信息
        membership.setLevelId(levelId);
        membership.setStatus(1);
        membership.setStartTime(LocalDateTime.now());
        membership.setEndTime(endTime);
        membership.setIsTrial(false);
        membership.setRenewType(renewType);
        membership.setUpdatedAt(LocalDateTime.now());
        
        int result = userMembershipMapper.updateById(membership);
        
        if (result > 0) {
            // 记录升级日志
            log.info("用户 {} 升级到会员等级 {}, 订单号: {}", userId, newLevel.getLevelName(), orderNo);
            
            // 添加升级奖励积分
            addUserPoints(userId, 100, "UPGRADE", levelId, "会员升级奖励");
        }
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public Boolean renewMembership(Long userId, Integer renewType, String orderNo) {
        UserMembership membership = getUserMembership(userId);
        if (membership == null) {
            log.error("用户会员记录不存在: {}", userId);
            return false;
        }
        
        // 计算新的结束时间
        LocalDateTime newEndTime;
        LocalDateTime currentEndTime = membership.getEndTime();
        if (currentEndTime == null || currentEndTime.isBefore(LocalDateTime.now())) {
            // 如果已过期，从当前时间开始计算
            newEndTime = LocalDateTime.now();
        } else {
            // 如果未过期，从原结束时间开始计算
            newEndTime = currentEndTime;
        }
        
        if (renewType == 1) {
            newEndTime = newEndTime.plusMonths(1);
        } else if (renewType == 2) {
            newEndTime = newEndTime.plusYears(1);
        }
        
        // 更新会员信息
        membership.setStatus(1);
        membership.setEndTime(newEndTime);
        membership.setRenewType(renewType);
        membership.setUpdatedAt(LocalDateTime.now());
        
        int result = userMembershipMapper.updateById(membership);
        
        if (result > 0) {
            log.info("用户 {} 续费会员，新结束时间: {}, 订单号: {}", userId, newEndTime, orderNo);
            
            // 添加续费奖励积分
            addUserPoints(userId, 50, "RENEW", null, "会员续费奖励");
        }
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public Boolean activateTrialMembership(Long userId, Long levelId) {
        UserMembership membership = getUserMembership(userId);
        if (membership == null) {
            log.error("用户会员记录不存在: {}", userId);
            return false;
        }
        
        MembershipLevel level = getLevelById(levelId);
        if (level == null || level.getTrialDays() == null || level.getTrialDays() <= 0) {
            log.error("会员等级不支持试用: {}", levelId);
            return false;
        }
        
        // 检查是否已经试用过
        if (Boolean.TRUE.equals(membership.getIsTrial())) {
            log.error("用户已经试用过会员: {}", userId);
            return false;
        }
        
        // 激活试用
        LocalDateTime trialEndTime = LocalDateTime.now().plusDays(level.getTrialDays());
        membership.setLevelId(levelId);
        membership.setStatus(1);
        membership.setIsTrial(true);
        membership.setTrialEndTime(trialEndTime);
        membership.setEndTime(trialEndTime);
        membership.setUpdatedAt(LocalDateTime.now());
        
        int result = userMembershipMapper.updateById(membership);
        
        if (result > 0) {
            log.info("用户 {} 激活试用会员，试用结束时间: {}", userId, trialEndTime);
            
            // 添加试用奖励积分
            addUserPoints(userId, 200, "TRIAL", levelId, "试用会员奖励");
        }
        
        return result > 0;
    }
    
    @Override
    public void checkAndUpdateExpiredMemberships() {
        // 查询所有过期的会员
        LambdaQueryWrapper<UserMembership> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMembership::getStatus, 1)
                .lt(UserMembership::getEndTime, LocalDateTime.now());
        
        List<UserMembership> expiredMemberships = userMembershipMapper.selectList(wrapper);
        
        for (UserMembership membership : expiredMemberships) {
            // 更新为过期状态
            membership.setStatus(2);
            membership.setUpdatedAt(LocalDateTime.now());
            
            // 如果不是自动续费，则降级到免费会员
            if (!Boolean.TRUE.equals(membership.getAutoRenew())) {
                MembershipLevel freeLevel = getLevelByCode("FREE");
                if (freeLevel != null) {
                    membership.setLevelId(freeLevel.getId());
                    membership.setEndTime(null);
                }
            }
            
            userMembershipMapper.updateById(membership);
            log.info("更新过期会员状态: 用户ID {}", membership.getUserId());
        }
    }
    
    @Override
    public Integer getUserPoints(Long userId) {
        UserMembership membership = getUserMembership(userId);
        return membership != null ? membership.getCurrentPoints() : 0;
    }
    
    @Override
    @Transactional
    public Boolean addUserPoints(Long userId, Integer points, String sourceType, Long sourceId, String description) {
        if (points <= 0) {
            return false;
        }
        
        UserMembership membership = getUserMembership(userId);
        if (membership == null) {
            return false;
        }
        
        // 更新会员积分
        membership.setPointsEarned((membership.getPointsEarned() != null ? membership.getPointsEarned() : 0) + points);
        membership.setUpdatedAt(LocalDateTime.now());
        userMembershipMapper.updateById(membership);
        
        // 记录积分变动
        MembershipPoint pointRecord = new MembershipPoint();
        pointRecord.setUserId(userId);
        pointRecord.setPointsType(1); // 获得
        pointRecord.setPointsAmount(points);
        pointRecord.setSourceType(sourceType);
        pointRecord.setSourceId(sourceId);
        pointRecord.setDescription(description);
        pointRecord.setBalanceAfter(membership.getCurrentPoints());
        pointRecord.setExpireTime(LocalDateTime.now().plusYears(1)); // 1年后过期
        pointRecord.setCreatedAt(LocalDateTime.now());
        
        return membershipPointMapper.insert(pointRecord) > 0;
    }
    
    @Override
    @Transactional
    public Boolean deductUserPoints(Long userId, Integer points, String sourceType, Long sourceId, String description) {
        if (points <= 0) {
            return false;
        }
        
        UserMembership membership = getUserMembership(userId);
        if (membership == null || membership.getCurrentPoints() < points) {
            return false;
        }
        
        // 更新会员积分
        membership.setPointsUsed((membership.getPointsUsed() != null ? membership.getPointsUsed() : 0) + points);
        membership.setUpdatedAt(LocalDateTime.now());
        userMembershipMapper.updateById(membership);
        
        // 记录积分变动
        MembershipPoint pointRecord = new MembershipPoint();
        pointRecord.setUserId(userId);
        pointRecord.setPointsType(2); // 消费
        pointRecord.setPointsAmount(points);
        pointRecord.setSourceType(sourceType);
        pointRecord.setSourceId(sourceId);
        pointRecord.setDescription(description);
        pointRecord.setBalanceAfter(membership.getCurrentPoints());
        pointRecord.setCreatedAt(LocalDateTime.now());
        
        return membershipPointMapper.insert(pointRecord) > 0;
    }
    
    @Override
    public IPage<MembershipPoint> getUserPointsHistory(Long userId, Page<MembershipPoint> page) {
        LambdaQueryWrapper<MembershipPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MembershipPoint::getUserId, userId)
                .orderByDesc(MembershipPoint::getCreatedAt);
        return membershipPointMapper.selectPage(page, wrapper);
    }
    
    @Override
    public String generateReferralCode(Long userId) {
        return "REF" + String.format("%08d", userId) + String.format("%04d", (int)(Math.random() * 10000));
    }
    
    @Override
    @Transactional
    public Boolean processReferralReward(Long referrerUserId, Long newUserId) {
        // 为推荐人添加奖励积分
        Boolean referrerReward = addUserPoints(referrerUserId, 500, "REFERRAL", newUserId, "推荐新用户奖励");
        
        // 为新用户添加注册奖励积分
        Boolean newUserReward = addUserPoints(newUserId, 100, "REGISTER", referrerUserId, "新用户注册奖励");
        
        return referrerReward && newUserReward;
    }
    
    @Override
    public Boolean hasPermission(Long userId, String permission) {
        UserMembership membership = getUserMembership(userId);
        if (membership == null || !membership.isValid()) {
            return false;
        }
        
        MembershipLevel level = getLevelById(membership.getLevelId());
        if (level == null) {
            return false;
        }
        
        // 根据权限类型检查
        switch (permission) {
            case "CREATE_GROUP":
                return level.getCanCreateGroup();
            case "UPLOAD_FILE":
                return level.getCanUploadFile();
            case "VIDEO_CALL":
                return level.getCanVideoCall();
            case "VOICE_CALL":
                return level.getCanVoiceCall();
            case "LIVE_STREAM":
                return level.getCanLiveStream();
            case "AD_FREE":
                return level.getAdFree();
            case "PRIORITY_SUPPORT":
                return level.getPrioritySupport();
            case "CUSTOM_THEME":
                return level.getCustomTheme();
            default:
                return false;
        }
    }
    
    @Override
    public List<String> getUserBenefits(Long userId) {
        UserMembership membership = getUserMembership(userId);
        if (membership == null) {
            return List.of();
        }
        
        LambdaQueryWrapper<MembershipBenefit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MembershipBenefit::getLevelId, membership.getLevelId())
                .eq(MembershipBenefit::getIsActive, true)
                .orderByAsc(MembershipBenefit::getSortOrder);
        
        List<MembershipBenefit> benefits = membershipBenefitMapper.selectList(wrapper);
        return benefits.stream()
                .map(MembershipBenefit::getBenefitName)
                .collect(Collectors.toList());
    }
}