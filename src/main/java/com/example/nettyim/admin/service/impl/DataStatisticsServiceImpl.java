package com.example.nettyim.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.admin.entity.DataStatistics;
import com.example.nettyim.admin.mapper.DataStatisticsMapper;
import com.example.nettyim.admin.service.DataStatisticsService;
import com.example.nettyim.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计服务实现类
 */
@Service
public class DataStatisticsServiceImpl extends ServiceImpl<DataStatisticsMapper, DataStatistics> implements DataStatisticsService {
    
    @Autowired
    private DataStatisticsMapper dataStatisticsMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MomentMapper momentMapper;
    
    @Autowired
    private ForumPostMapper forumPostMapper;
    
    @Autowired
    private PaymentOrderMapper paymentOrderMapper;
    
    @Autowired
    private UserMembershipMapper userMembershipMapper;
    
    @Override
    public IPage<DataStatistics> pageStatistics(Page<DataStatistics> page, String statType, String startTime, String endTime) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        if (startTime != null && !startTime.isEmpty()) {
            start = LocalDateTime.parse(startTime, formatter);
        }
        
        if (endTime != null && !endTime.isEmpty()) {
            end = LocalDateTime.parse(endTime, formatter);
        }
        
        return dataStatisticsMapper.selectStatisticsPage(page, statType, start, end);
    }
    
    @Override
    public Map<String, Object> getOverallStatistics(String startTime, String endTime) {
        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);
        
        Map<String, Object> result = new HashMap<>();
        
        // 获取各类统计数据
        Map<String, Object> userStats = getUserStatistics(startTime, endTime);
        Map<String, Object> contentStats = getContentStatistics(startTime, endTime);
        Map<String, Object> paymentStats = getPaymentStatistics(startTime, endTime);
        Map<String, Object> membershipStats = getMembershipStatistics(startTime, endTime);
        
        result.put("userStats", userStats);
        result.put("contentStats", contentStats);
        result.put("paymentStats", paymentStats);
        result.put("membershipStats", membershipStats);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getUserStatistics(String startTime, String endTime) {
        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);
        
        return dataStatisticsMapper.selectUserStatistics(start, end);
    }
    
    @Override
    public Map<String, Object> getContentStatistics(String startTime, String endTime) {
        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);
        
        return dataStatisticsMapper.selectContentStatistics(start, end);
    }
    
    @Override
    public Map<String, Object> getPaymentStatistics(String startTime, String endTime) {
        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);
        
        return dataStatisticsMapper.selectPaymentStatistics(start, end);
    }
    
    @Override
    public Map<String, Object> getMembershipStatistics(String startTime, String endTime) {
        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);
        
        return dataStatisticsMapper.selectMembershipStatistics(start, end);
    }
    
    @Override
    public Map<String, Object> getChartData(String chartType, String startTime, String endTime) {
        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);
        
        Map<String, Object> result = new HashMap<>();
        
        switch (chartType) {
            case "activeUsers":
                List<Map<String, Object>> dailyActiveUsers = dataStatisticsMapper.selectDailyActiveUsers(start, end);
                result.put("data", dailyActiveUsers);
                break;
            default:
                result.put("data", new HashMap<>());
                break;
        }
        
        return result;
    }
    
    @Override
    public boolean generateDailyStatistics(String date) {
        try {
            LocalDate statDate = LocalDate.parse(date);
            LocalDateTime startOfDay = statDate.atStartOfDay();
            LocalDateTime endOfDay = statDate.atTime(23, 59, 59);
            
            DataStatistics statistics = new DataStatistics();
            statistics.setStatType("daily");
            statistics.setStatDate(startOfDay);
            
            // 获取用户统计数据
            Map<String, Object> userStats = dataStatisticsMapper.selectUserStatistics(startOfDay, endOfDay);
            statistics.setNewUsers(((Number) userStats.get("newUsers")).intValue());
            statistics.setTotalUsers(((Number) userStats.get("totalUsers")).intValue());
            
            // 获取内容统计数据
            Map<String, Object> contentStats = dataStatisticsMapper.selectContentStatistics(startOfDay, endOfDay);
            statistics.setNewContents(((Number) contentStats.get("newContents")).intValue());
            statistics.setTotalContents(((Number) contentStats.get("totalContents")).intValue());
            
            // 获取支付统计数据
            Map<String, Object> paymentStats = dataStatisticsMapper.selectPaymentStatistics(startOfDay, endOfDay);
            statistics.setNewOrders(((Number) paymentStats.get("newOrders")).intValue());
            statistics.setTotalOrders(((Number) paymentStats.get("totalOrders")).intValue());
            statistics.setOrderAmount(new BigDecimal(paymentStats.get("orderAmount").toString()));
            
            // 获取会员统计数据
            Map<String, Object> membershipStats = dataStatisticsMapper.selectMembershipStatistics(startOfDay, endOfDay);
            statistics.setNewMembers(((Number) membershipStats.get("newMembers")).intValue());
            statistics.setTotalMembers(((Number) membershipStats.get("totalMembers")).intValue());
            
            // 检查是否已存在同一天的统计数据
            QueryWrapper<DataStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stat_type", "daily");
            queryWrapper.eq("DATE(stat_date)", date);
            
            DataStatistics existing = getOne(queryWrapper);
            if (existing != null) {
                statistics.setId(existing.getId());
                return updateById(statistics);
            } else {
                return save(statistics);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public DataStatistics getById(Long id) {
        return dataStatisticsMapper.selectById(id);
    }
    
    @Override
    public boolean deleteStatistics(Long id) {
        return dataStatisticsMapper.deleteById(id) > 0;
    }
    
    /**
     * 解析日期时间字符串
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}