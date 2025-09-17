package com.example.nettyim.service;

import com.example.nettyim.dto.CreateForumDTO;
import com.example.nettyim.dto.CreateForumPostDTO;
import com.example.nettyim.dto.ForumPostQueryDTO;
import com.example.nettyim.dto.ForumQueryDTO;
import com.example.nettyim.dto.ForumReplyDTO;
import com.example.nettyim.entity.Forum;
import com.example.nettyim.entity.ForumMember;
import com.example.nettyim.entity.ForumPost;
import com.example.nettyim.entity.ForumReply;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 贴吧服务接口
 */
public interface ForumService {
    
    /**
     * 创建贴吧
     */
    Forum createForum(Long userId, CreateForumDTO createDTO);
    
    /**
     * 加入贴吧
     */
    boolean joinForum(Long userId, Long forumId);
    
    /**
     * 退出贴吧
     */
    boolean leaveForum(Long userId, Long forumId);
    
    /**
     * 分页查询贴吧列表
     */
    Page<Forum> getForums(ForumQueryDTO queryDTO);
    
    /**
     * 获取贴吧详情
     */
    Forum getForumById(Long forumId);
    
    /**
     * 获取贴吧成员列表
     */
    Page<ForumMember> getForumMembers(Long forumId, Integer page, Integer size);
    
    /**
     * 检查用户是否为贴吧成员
     */
    boolean isMember(Long userId, Long forumId);
    
    /**
     * 获取用户在贴吧中的角色
     */
    Integer getUserRole(Long userId, Long forumId);
    
    /**
     * 设置用户角色（仅吧主和管理员可操作）
     */
    boolean setUserRole(Long operatorId, Long forumId, Long userId, Integer role);
    
    /**
     * 发布帖子
     */
    ForumPost createPost(Long userId, CreateForumPostDTO postDTO);
    
    /**
     * 删除帖子
     */
    boolean deletePost(Long userId, Long postId);
    
    /**
     * 分页查询帖子列表
     */
    Page<ForumPost> getPosts(ForumPostQueryDTO queryDTO);
    
    /**
     * 获取帖子详情（会增加浏览数）
     */
    ForumPost getPostById(Long postId);
    
    /**
     * 置顶/取消置顶帖子
     */
    boolean pinPost(Long operatorId, Long postId, boolean pin);
    
    /**
     * 设置/取消精华帖
     */
    boolean setEssencePost(Long operatorId, Long postId, boolean essence);
    
    /**
     * 点赞/取消点赞帖子
     */
    boolean likePost(Long userId, Long postId);
    
    /**
     * 回复帖子
     */
    ForumReply replyPost(Long userId, ForumReplyDTO replyDTO);
    
    /**
     * 删除回复
     */
    boolean deleteReply(Long userId, Long replyId);
    
    /**
     * 获取帖子回复列表
     */
    Page<ForumReply> getPostReplies(Long postId, Integer page, Integer size);
    
    /**
     * 点赞/取消点赞回复
     */
    boolean likeReply(Long userId, Long replyId);
    
    /**
     * 检查用户是否已点赞帖子
     */
    boolean isPostLikedByUser(Long userId, Long postId);
    
    /**
     * 检查用户是否已点赞回复
     */
    boolean isReplyLikedByUser(Long userId, Long replyId);
}