package com.example.nettyim.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.admin.service.SecurityLogService;
import com.example.nettyim.entity.SecurityLog;
import com.example.nettyim.mapper.SecurityLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 安全日志服务实现类
 */
@Service
public class SecurityLogServiceImpl extends ServiceImpl<SecurityLogMapper, SecurityLog> implements SecurityLogService {
    
    @Autowired
    private SecurityLogMapper securityLogMapper;
    
    @Override
    public IPage<SecurityLog> pageSecurityLogs(Page<SecurityLog> page, Long userId, String actionType, Integer riskLevel, String startTime, String endTime) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        if (startTime != null && !startTime.isEmpty()) {
            start = LocalDateTime.parse(startTime, formatter);
        }
        
        if (endTime != null && !endTime.isEmpty()) {
            end = LocalDateTime.parse(endTime, formatter);
        }
        
        return securityLogMapper.selectSecurityLogPage(page, userId, actionType, riskLevel, start, end);
    }
    
    @Override
    public Map<String, Object> getSecurityLogStatistics(String startTime, String endTime) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        if (startTime != null && !startTime.isEmpty()) {
            start = LocalDateTime.parse(startTime, formatter);
        }
        
        if (endTime != null && !endTime.isEmpty()) {
            end = LocalDateTime.parse(endTime, formatter);
        }
        
        if (start == null) {
            start = LocalDateTime.now().minusDays(7); // 默认查询最近7天
        }
        
        if (end == null) {
            end = LocalDateTime.now();
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 获取操作类型统计
        List<Map<String, Object>> actionTypeStats = securityLogMapper.selectSecurityLogStatistics(start, end);
        result.put("actionTypeStats", actionTypeStats);
        
        // 获取风险等级统计
        List<Map<String, Object>> riskLevelStats = securityLogMapper.selectRiskLevelStatistics(start, end);
        result.put("riskLevelStats", riskLevelStats);
        
        return result;
    }
    
    @Override
    public SecurityLog getById(Long id) {
        return securityLogMapper.selectById(id);
    }
    
    @Override
    public boolean deleteSecurityLog(Long id) {
        return securityLogMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean batchDeleteSecurityLogs(String ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        
        return securityLogMapper.deleteBatchIds(idList) > 0;
    }
}