package com.example.nettyim.service;

import com.example.nettyim.dto.MomentCommentDTO;
import com.example.nettyim.dto.MomentQueryDTO;
import com.example.nettyim.dto.PublishMomentDTO;
import com.example.nettyim.entity.Moment;
import com.example.nettyim.entity.MomentComment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 朋友圈服务接口
 */
public interface MomentService {
    
    /**
     * 发布朋友圈动态
     */
    Moment publishMoment(Long userId, PublishMomentDTO publishDTO);
    
    /**
     * 删除朋友圈动态
     */
    void deleteMoment(Long userId, Long momentId);
    
    /**
     * 分页查询朋友圈动态
     */
    Page<Moment> getMoments(MomentQueryDTO queryDTO);
    
    /**
     * 获取动态详情
     */
    Moment getMomentById(Long momentId);
    
    /**
     * 点赞/取消点赞动态
     */
    boolean likeMoment(Long userId, Long momentId);
    
    /**
     * 评论动态
     */
    MomentComment commentMoment(Long userId, MomentCommentDTO commentDTO);
    
    /**
     * 删除评论
     */
    void deleteComment(Long userId, Long commentId);
    
    /**
     * 获取动态的评论列表
     */
    Page<MomentComment> getMomentComments(Long momentId, Integer page, Integer size);
    
    /**
     * 检查用户是否已点赞动态
     */
    boolean isLikedByUser(Long userId, Long momentId);
}