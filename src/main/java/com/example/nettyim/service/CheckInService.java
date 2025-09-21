package com.example.nettyim.service;

import com.example.nettyim.entity.UserCheckIn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 签到服务接口
 */
public interface CheckInService {
    
    /**
     * 用户签到
     */
    UserCheckIn checkIn(Long userId);
    
    /**
     * 获取用户签到记录
     */
    List<UserCheckIn> getUserCheckInHistory(Long userId, Integer limit);
    
    /**
     * 获取用户连续签到天数
     */
    Integer getContinuousCheckInDays(Long userId);
    
    /**
     * 检查用户今日是否已签到
     */
    Boolean isCheckedInToday(Long userId);
    
    /**
     * 获取签到奖励规则
     */
    Map<Integer, Map<String, Object>> getCheckInRewards();
    
    /**
     * 计算签到奖励
     */
    Map<String, Object> calculateCheckInReward(Integer continuousDays);
    
    /**
     * 获取签到统计信息
     */
    Map<String, Object> getCheckInStats(Long userId);
    
    /**
     * 获取本月签到日历
     */
    Map<String, Object> getMonthlyCheckInCalendar(Long userId, LocalDate month);
    
    /**
     * 补签（会员特权）
     */
    Boolean makeUpCheckIn(Long userId, LocalDate date);
    
    /**
     * 获取可补签天数
     */
    Integer getAvailableMakeUpDays(Long userId);
}