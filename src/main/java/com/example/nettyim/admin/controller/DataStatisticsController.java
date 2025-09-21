package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.entity.DataStatistics;
import com.example.nettyim.admin.service.DataStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据统计控制器
 */
@RestController
@RequestMapping("/admin/api/statistics")
public class DataStatisticsController {
    
    @Autowired
    private DataStatisticsService dataStatisticsService;
    
    /**
     * 分页查询数据统计列表
     */
    @GetMapping
    public Map<String, Object> pageStatistics(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String statType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Page<DataStatistics> pageRequest = new Page<>(page, size);
        IPage<DataStatistics> pageResult = dataStatisticsService.pageStatistics(pageRequest, statType, startTime, endTime);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取综合统计数据
     */
    @GetMapping("/overview")
    public Map<String, Object> getOverallStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> statistics = dataStatisticsService.getOverallStatistics(startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", statistics);
        return result;
    }
    
    /**
     * 获取用户统计数据
     */
    @GetMapping("/users")
    public Map<String, Object> getUserStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> statistics = dataStatisticsService.getUserStatistics(startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", statistics);
        return result;
    }
    
    /**
     * 获取内容统计数据
     */
    @GetMapping("/contents")
    public Map<String, Object> getContentStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> statistics = dataStatisticsService.getContentStatistics(startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", statistics);
        return result;
    }
    
    /**
     * 获取支付统计数据
     */
    @GetMapping("/payments")
    public Map<String, Object> getPaymentStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> statistics = dataStatisticsService.getPaymentStatistics(startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", statistics);
        return result;
    }
    
    /**
     * 获取会员统计数据
     */
    @GetMapping("/memberships")
    public Map<String, Object> getMembershipStatistics(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> statistics = dataStatisticsService.getMembershipStatistics(startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", statistics);
        return result;
    }
    
    /**
     * 获取图表数据
     */
    @GetMapping("/chart")
    public Map<String, Object> getChartData(
            @RequestParam String chartType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> chartData = dataStatisticsService.getChartData(chartType, startTime, endTime);
        
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", chartData);
        return result;
    }
    
    /**
     * 生成每日统计数据
     */
    @PostMapping("/generate")
    public Map<String, Object> generateDailyStatistics(@RequestParam String date) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = dataStatisticsService.generateDailyStatistics(date);
        if (success) {
            result.put("success", true);
            result.put("message", "统计数据生成成功");
        } else {
            result.put("success", false);
            result.put("message", "统计数据生成失败");
        }
        return result;
    }
    
    /**
     * 根据ID获取统计数据
     */
    @GetMapping("/{id}")
    public Map<String, Object> getStatisticsById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        DataStatistics statistics = dataStatisticsService.getById(id);
        if (statistics != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", statistics);
        } else {
            result.put("success", false);
            result.put("message", "统计数据不存在");
        }
        return result;
    }
    
    /**
     * 删除统计数据
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteStatistics(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = dataStatisticsService.deleteStatistics(id);
        if (success) {
            result.put("success", true);
            result.put("message", "删除成功");
        } else {
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }
    
    /**
     * 构造分页结果
     */
    private Map<String, Object> getPageResultMap(IPage<DataStatistics> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        return result;
    }
}