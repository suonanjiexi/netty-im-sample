package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.ContentAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 内容审核Mapper
 */
@Mapper
public interface ContentAuditMapper extends BaseMapper<ContentAudit> {
    
    /**
     * 分页查询审核记录
     */
    @Select("SELECT * FROM content_audits WHERE " +
            "(@contentType IS NULL OR content_type = @contentType) AND " +
            "(@auditStatus IS NULL OR audit_status = @auditStatus) AND " +
            "(@userId IS NULL OR user_id = @userId) AND " +
            "(@auditorId IS NULL OR auditor_id = @auditorId) AND " +
            "(@startTime IS NULL OR created_at >= @startTime) AND " +
            "(@endTime IS NULL OR created_at <= @endTime) " +
            "ORDER BY created_at DESC")
    IPage<ContentAudit> selectAuditPage(Page<ContentAudit> page,
                                        @Param("contentType") Integer contentType,
                                        @Param("auditStatus") Integer auditStatus,
                                        @Param("userId") Long userId,
                                        @Param("auditorId") Long auditorId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查询待审核记录数量
     */
    @Select("SELECT COUNT(*) FROM content_audits WHERE audit_status = 0")
    Long countPendingAudits();
    
    /**
     * 查询用户的审核记录
     */
    @Select("SELECT * FROM content_audits WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
    List<ContentAudit> selectByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 查询指定内容的审核记录
     */
    @Select("SELECT * FROM content_audits WHERE content_type = #{contentType} AND content_id = #{contentId}")
    ContentAudit selectByContentTypeAndId(@Param("contentType") Integer contentType, @Param("contentId") Long contentId);
    
    /**
     * 统计审核数据
     */
    @Select("SELECT audit_status, COUNT(*) as count FROM content_audits " +
            "WHERE created_at >= #{startTime} AND created_at <= #{endTime} " +
            "GROUP BY audit_status")
    List<java.util.Map<String, Object>> selectAuditStatistics(@Param("startTime") LocalDateTime startTime,
                                                               @Param("endTime") LocalDateTime endTime);
}