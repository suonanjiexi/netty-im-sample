package com.example.nettyim.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nettyim.admin.entity.AdminOperationLog;
import com.example.nettyim.admin.mapper.AdminOperationLogMapper;
import com.example.nettyim.admin.service.AdminOperationLogService;
import org.springframework.stereotype.Service;

/**
 * 管理员操作日志服务实现类
 */
@Service
public class AdminOperationLogServiceImpl extends ServiceImpl<AdminOperationLogMapper, AdminOperationLog> implements AdminOperationLogService {
    
    @Override
    public IPage<AdminOperationLog> pageOperationLogs(Page<AdminOperationLog> page, Long adminUserId, String module, String operation) {
        QueryWrapper<AdminOperationLog> queryWrapper = new QueryWrapper<>();
        
        if (adminUserId != null) {
            queryWrapper.eq("admin_user_id", adminUserId);
        }
        
        if (module != null && !module.isEmpty()) {
            queryWrapper.like("module", module);
        }
        
        if (operation != null && !operation.isEmpty()) {
            queryWrapper.like("operation", operation);
        }
        
        queryWrapper.orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
    
    @Override
    public boolean saveOperationLog(AdminOperationLog log) {
        return this.save(log);
    }
    
    @Override
    public AdminOperationLog getById(Long id) {
        return this.baseMapper.selectById(id);
    }
    
    @Override
    public boolean deleteOperationLog(Long id) {
        return this.removeById(id);
    }
}