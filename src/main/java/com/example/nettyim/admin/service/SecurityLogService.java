package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.SecurityLog;

import java.util.Map;

/**
 * 安全日志服务接口
 */
public interface SecurityLogService {
    
    /**
     * 分页查询安全日志列表
     * @param page 分页参数
     * @param userId 用户ID
     * @param actionType 操作类型
     * @param riskLevel 风险等级
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 安全日志分页列表
     */
    IPage<SecurityLog> pageSecurityLogs(Page<SecurityLog> page, Long userId, String actionType, Integer riskLevel, String startTime, String endTime);
    
    /**
     * 获取安全日志统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    Map<String, Object> getSecurityLogStatistics(String startTime, String endTime);
    
    /**
     * 根据ID获取安全日志
     * @param id 日志ID
     * @return 安全日志
     */
    SecurityLog getById(Long id);
    
    /**
     * 删除安全日志
     * @param id 日志ID
     * @return 是否成功
     */
    boolean deleteSecurityLog(Long id);
    
    /**
     * 批量删除安全日志
     * @param ids 日志ID列表
     * @return 是否成功
     */
    boolean batchDeleteSecurityLogs(String ids);
}