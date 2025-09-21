package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.entity.DataStatistics;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据统计服务接口
 */
public interface DataStatisticsService {
    
    /**
     * 分页查询数据统计列表
     * @param page 分页参数
     * @param statType 统计类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 数据统计分页列表
     */
    IPage<DataStatistics> pageStatistics(Page<DataStatistics> page, String statType, String startTime, String endTime);
    
    /**
     * 获取综合统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 综合统计数据
     */
    Map<String, Object> getOverallStatistics(String startTime, String endTime);
    
    /**
     * 获取用户统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户统计数据
     */
    Map<String, Object> getUserStatistics(String startTime, String endTime);
    
    /**
     * 获取内容统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 内容统计数据
     */
    Map<String, Object> getContentStatistics(String startTime, String endTime);
    
    /**
     * 获取支付统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 支付统计数据
     */
    Map<String, Object> getPaymentStatistics(String startTime, String endTime);
    
    /**
     * 获取会员统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 会员统计数据
     */
    Map<String, Object> getMembershipStatistics(String startTime, String endTime);
    
    /**
     * 获取图表数据
     * @param chartType 图表类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 图表数据
     */
    Map<String, Object> getChartData(String chartType, String startTime, String endTime);
    
    /**
     * 生成每日统计数据
     * @param date 统计日期
     * @return 是否成功
     */
    boolean generateDailyStatistics(String date);
    
    /**
     * 根据ID获取统计数据
     * @param id 统计ID
     * @return 统计数据
     */
    DataStatistics getById(Long id);
    
    /**
     * 删除统计数据
     * @param id 统计ID
     * @return 是否成功
     */
    boolean deleteStatistics(Long id);
}