package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.SecurityLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 安全日志Mapper接口
 */
@Mapper
public interface SecurityLogMapper extends BaseMapper<SecurityLog> {
    
    /**
     * 分页查询安全日志列表
     */
    @Select("SELECT * FROM security_logs WHERE " +
            "(@userId IS NULL OR user_id = @userId) AND " +
            "(@actionType IS NULL OR action_type = @actionType) AND " +
            "(@riskLevel IS NULL OR risk_level = @riskLevel) AND " +
            "(@startTime IS NULL OR created_at >= @startTime) AND " +
            "(@endTime IS NULL OR created_at <= @endTime) " +
            "ORDER BY created_at DESC")
    IPage<SecurityLog> selectSecurityLogPage(Page<SecurityLog> page,
                                           @Param("userId") Long userId,
                                           @Param("actionType") String actionType,
                                           @Param("riskLevel") Integer riskLevel,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计安全日志数据
     */
    @Select("SELECT action_type, COUNT(*) as count FROM security_logs " +
            "WHERE created_at >= #{startTime} AND created_at <= #{endTime} " +
            "GROUP BY action_type")
    List<Map<String, Object>> selectSecurityLogStatistics(@Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 按风险等级统计安全日志
     */
    @Select("SELECT risk_level, COUNT(*) as count FROM security_logs " +
            "WHERE created_at >= #{startTime} AND created_at <= #{endTime} " +
            "GROUP BY risk_level")
    List<Map<String, Object>> selectRiskLevelStatistics(@Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime);
}