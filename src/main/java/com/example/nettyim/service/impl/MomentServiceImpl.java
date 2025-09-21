package com.example.nettyim.service.impl;

import com.example.nettyim.dto.MomentCommentDTO;
import com.example.nettyim.dto.MomentQueryDTO;
import com.example.nettyim.dto.PublishMomentDTO;
import com.example.nettyim.entity.ContentAudit;
import com.example.nettyim.entity.Moment;
import com.example.nettyim.entity.MomentComment;
import com.example.nettyim.entity.MomentLike;
import com.example.nettyim.entity.enums.AuditStatus;
import com.example.nettyim.entity.enums.ContentType;
import com.example.nettyim.exception.BusinessException;
import com.example.nettyim.mapper.MomentCommentMapper;
import com.example.nettyim.mapper.MomentLikeMapper;
import com.example.nettyim.mapper.MomentMapper;
import com.example.nettyim.service.ContentAuditService;
import com.example.nettyim.service.MomentService;
import com.example.nettyim.service.SensitiveWordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 朋友圈服务实现
 */
@Service
@Transactional
public class MomentServiceImpl implements MomentService {
    
    private final MomentMapper momentMapper;
    private final MomentCommentMapper momentCommentMapper;
    private final MomentLikeMapper momentLikeMapper;
    private final ObjectMapper objectMapper;
    private final ContentAuditService contentAuditService;
    private final SensitiveWordService sensitiveWordService;
    
    public MomentServiceImpl(MomentMapper momentMapper, 
                           MomentCommentMapper momentCommentMapper,
                           MomentLikeMapper momentLikeMapper,
                           ObjectMapper objectMapper,
                           ContentAuditService contentAuditService,
                           SensitiveWordService sensitiveWordService) {
        this.momentMapper = momentMapper;
        this.momentCommentMapper = momentCommentMapper;
        this.momentLikeMapper = momentLikeMapper;
        this.objectMapper = objectMapper;
        this.contentAuditService = contentAuditService;
        this.sensitiveWordService = sensitiveWordService;
    }
    
    @Override
    public Moment publishMoment(Long userId, PublishMomentDTO publishDTO) {
        Moment moment = new Moment();
        moment.setUserId(userId);
        moment.setContent(publishDTO.getContent());
        moment.setLocation(publishDTO.getLocation());
        moment.setVisibility(publishDTO.getVisibility());
        moment.setLikeCount(0);
        moment.setCommentCount(0);
        
        // 内容审核
        ContentAudit auditResult = contentAuditService.autoAuditContent(
            ContentType.MOMENT, null, userId, publishDTO.getContent());
        
        if (auditResult != null) {
            moment.setAuditStatus(auditResult.getAuditStatus());
            // 如果审核被拒绝，过滤敏感词后再发布
            if (AuditStatus.AUTO_REJECTED.getCode().equals(auditResult.getAuditStatus())) {
                String filteredContent = sensitiveWordService.filterSensitiveWords(publishDTO.getContent());
                moment.setContent(filteredContent);
                moment.setAuditStatus(AuditStatus.AUTO_APPROVED.getCode());
            }
        } else {
            moment.setAuditStatus(AuditStatus.AUTO_APPROVED.getCode());
        }
        
        // 处理图片列表，转换为JSON格式存储
        if (publishDTO.getImages() != null && !publishDTO.getImages().isEmpty()) {
            try {
                String imagesJson = objectMapper.writeValueAsString(publishDTO.getImages());
                moment.setImages(imagesJson);
            } catch (JsonProcessingException e) {
                throw new BusinessException("图片信息处理失败");
            }
        }
        
        momentMapper.insert(moment);
        
        // 更新审核记录的内容ID
        ContentAudit audit = contentAuditService.getContentAuditRecord(ContentType.MOMENT, moment.getId());
        if (audit == null) {
            contentAuditService.autoAuditContent(ContentType.MOMENT, moment.getId(), userId, publishDTO.getContent());
        }
        
        return moment;
    }
    
    @Override
    public void deleteMoment(Long userId, Long momentId) {
        Moment moment = momentMapper.selectById(momentId);
        
        if (moment == null) {
            throw new BusinessException("动态不存在");
        }
        
        if (!moment.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除该动态");
        }
        
        // 删除动态
        momentMapper.deleteById(momentId);
        
        // 删除相关的评论和点赞
        momentCommentMapper.delete(new QueryWrapper<MomentComment>()
                .eq("moment_id", momentId));
        momentLikeMapper.delete(new QueryWrapper<MomentLike>()
                .eq("moment_id", momentId));
    }
    
    @Override
    public Page<Moment> getMoments(MomentQueryDTO queryDTO) {
        Page<Moment> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        QueryWrapper<Moment> wrapper = new QueryWrapper<>();
        
        // 根据用户ID筛选
        if (queryDTO.getUserId() != null) {
            wrapper.eq("user_id", queryDTO.getUserId());
        }
        
        // 根据可见性筛选
        if (queryDTO.getVisibility() != null) {
            wrapper.eq("visibility", queryDTO.getVisibility());
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc("created_at");
        
        return momentMapper.selectPage(page, wrapper);
    }
    
    @Override
    public Moment getMomentById(Long momentId) {
        Moment moment = momentMapper.selectById(momentId);
        
        if (moment == null) {
            throw new BusinessException("动态不存在");
        }
        
        return moment;
    }
    
    @Override
    public boolean likeMoment(Long userId, Long momentId) {
        // 检查动态是否存在
        Moment moment = getMomentById(momentId);
        
        // 检查是否已经点赞
        MomentLike existingLike = momentLikeMapper.selectOne(new QueryWrapper<MomentLike>()
                .eq("moment_id", momentId)
                .eq("user_id", userId));
        
        if (existingLike != null) {
            // 取消点赞
            momentLikeMapper.deleteById(existingLike.getId());
            
            // 更新点赞数
            moment.setLikeCount(Math.max(0, moment.getLikeCount() - 1));
            momentMapper.updateById(moment);
            
            return false;
        } else {
            // 添加点赞
            MomentLike like = new MomentLike();
            like.setMomentId(momentId);
            like.setUserId(userId);
            momentLikeMapper.insert(like);
            
            // 更新点赞数
            moment.setLikeCount(moment.getLikeCount() + 1);
            momentMapper.updateById(moment);
            
            return true;
        }
    }
    
    @Override
    public MomentComment commentMoment(Long userId, MomentCommentDTO commentDTO) {
        // 检查动态是否存在
        Moment moment = getMomentById(commentDTO.getMomentId());
        
        MomentComment comment = new MomentComment();
        comment.setMomentId(commentDTO.getMomentId());
        comment.setUserId(userId);
        comment.setContent(commentDTO.getContent());
        comment.setReplyToUserId(commentDTO.getReplyToUserId());
        comment.setReplyToCommentId(commentDTO.getReplyToCommentId());
        
        momentCommentMapper.insert(comment);
        
        // 更新评论数
        moment.setCommentCount(moment.getCommentCount() + 1);
        momentMapper.updateById(moment);
        
        return comment;
    }
    
    @Override
    public void deleteComment(Long userId, Long commentId) {
        MomentComment comment = momentCommentMapper.selectById(commentId);
        
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除该评论");
        }
        
        // 删除评论
        momentCommentMapper.deleteById(commentId);
        
        // 更新评论数
        Moment moment = getMomentById(comment.getMomentId());
        moment.setCommentCount(Math.max(0, moment.getCommentCount() - 1));
        momentMapper.updateById(moment);
    }
    
    @Override
    public Page<MomentComment> getMomentComments(Long momentId, Integer page, Integer size) {
        Page<MomentComment> commentPage = new Page<>(page, size);
        
        QueryWrapper<MomentComment> wrapper = new QueryWrapper<>();
        wrapper.eq("moment_id", momentId);
        wrapper.orderByAsc("created_at");
        
        return momentCommentMapper.selectPage(commentPage, wrapper);
    }
    
    @Override
    public boolean isLikedByUser(Long userId, Long momentId) {
        MomentLike like = momentLikeMapper.selectOne(new QueryWrapper<MomentLike>()
                .eq("moment_id", momentId)
                .eq("user_id", userId));
        
        return like != null;
    }
}