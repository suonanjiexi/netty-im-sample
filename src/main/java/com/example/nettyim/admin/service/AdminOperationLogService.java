package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.entity.AdminOperationLog;

/**
 * 管理员操作日志服务接口
 */
public interface AdminOperationLogService {
    
    /**
     * 分页查询操作日志列表
     * @param page 分页参数
     * @param adminUserId 管理员ID
     * @param module 模块
     * @param operation 操作类型
     * @return 操作日志分页列表
     */
    IPage<AdminOperationLog> pageOperationLogs(Page<AdminOperationLog> page, Long adminUserId, String module, String operation);
    
    /**
     * 记录操作日志
     * @param log 操作日志
     * @return 是否成功
     */
    boolean saveOperationLog(AdminOperationLog log);
    
    /**
     * 根据ID获取操作日志
     * @param id 日志ID
     * @return 操作日志
     */
    AdminOperationLog getById(Long id);
    
    /**
     * 删除操作日志
     * @param id 日志ID
     * @return 是否成功
     */
    boolean deleteOperationLog(Long id);
}