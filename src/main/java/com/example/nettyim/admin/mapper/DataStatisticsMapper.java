package com.example.nettyim.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.entity.DataStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据统计Mapper接口
 */
@Mapper
public interface DataStatisticsMapper extends BaseMapper<DataStatistics> {
    
    /**
     * 分页查询数据统计列表
     */
    @Select("SELECT * FROM data_statistics WHERE " +
            "(@statType IS NULL OR stat_type = @statType) AND " +
            "(@startTime IS NULL OR stat_date >= @startTime) AND " +
            "(@endTime IS NULL OR stat_date <= @endTime) " +
            "ORDER BY stat_date DESC")
    IPage<DataStatistics> selectStatisticsPage(Page<DataStatistics> page,
                                              @Param("statType") String statType,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取用户统计数据
     */
    @Select("SELECT " +
            "COUNT(CASE WHEN created_at >= #{startTime} AND created_at <= #{endTime} THEN 1 END) as newUsers, " +
            "COUNT(*) as totalUsers " +
            "FROM users")
    Map<String, Object> selectUserStatistics(@Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取内容统计数据
     */
    @Select("SELECT " +
            "COUNT(CASE WHEN created_at >= #{startTime} AND created_at <= #{endTime} THEN 1 END) as newContents, " +
            "COUNT(*) as totalContents " +
            "FROM (SELECT id, created_at FROM moments UNION ALL SELECT id, created_at FROM forum_posts) as contents")
    Map<String, Object> selectContentStatistics(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取支付统计数据
     */
    @Select("SELECT " +
            "COUNT(CASE WHEN created_at >= #{startTime} AND created_at <= #{endTime} THEN 1 END) as newOrders, " +
            "COUNT(*) as totalOrders, " +
            "COALESCE(SUM(amount), 0) as orderAmount " +
            "FROM payment_orders WHERE status = 1")
    Map<String, Object> selectPaymentStatistics(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取会员统计数据
     */
    @Select("SELECT " +
            "COUNT(CASE WHEN created_at >= #{startTime} AND created_at <= #{endTime} THEN 1 END) as newMembers, " +
            "COUNT(*) as totalMembers " +
            "FROM user_memberships WHERE status = 1")
    Map<String, Object> selectMembershipStatistics(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取最近N天的用户活跃数据
     */
    @Select("SELECT DATE(created_at) as date, COUNT(DISTINCT user_id) as activeUsers " +
            "FROM user_sessions " +
            "WHERE created_at >= #{startTime} AND created_at <= #{endTime} AND status = 1 " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY date")
    List<Map<String, Object>> selectDailyActiveUsers(@Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);
}