package com.example.nettyim.service.impl;

import com.example.nettyim.dto.CreateForumDTO;
import com.example.nettyim.dto.CreateForumPostDTO;
import com.example.nettyim.dto.ForumPostQueryDTO;
import com.example.nettyim.dto.ForumQueryDTO;
import com.example.nettyim.dto.ForumReplyDTO;
import com.example.nettyim.entity.*;
import com.example.nettyim.exception.BusinessException;
import com.example.nettyim.mapper.*;
import com.example.nettyim.service.ForumService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 贴吧服务实现
 */
@Service
@Transactional
public class ForumServiceImpl implements ForumService {
    
    private final ForumMapper forumMapper;
    private final ForumMemberMapper forumMemberMapper;
    private final ForumPostMapper forumPostMapper;
    private final ForumReplyMapper forumReplyMapper;
    private final ForumPostLikeMapper forumPostLikeMapper;
    private final ForumReplyLikeMapper forumReplyLikeMapper;
    private final ObjectMapper objectMapper;
    
    public ForumServiceImpl(ForumMapper forumMapper,
                           ForumMemberMapper forumMemberMapper,
                           ForumPostMapper forumPostMapper,
                           ForumReplyMapper forumReplyMapper,
                           ForumPostLikeMapper forumPostLikeMapper,
                           ForumReplyLikeMapper forumReplyLikeMapper,
                           ObjectMapper objectMapper) {
        this.forumMapper = forumMapper;
        this.forumMemberMapper = forumMemberMapper;
        this.forumPostMapper = forumPostMapper;
        this.forumReplyMapper = forumReplyMapper;
        this.forumPostLikeMapper = forumPostLikeMapper;
        this.forumReplyLikeMapper = forumReplyLikeMapper;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public Forum createForum(Long userId, CreateForumDTO createDTO) {
        // 检查贴吧名称是否已存在
        Forum existingForum = forumMapper.selectOne(new QueryWrapper<Forum>()
                .eq("name", createDTO.getName())
                .eq("status", 1));
        
        if (existingForum != null) {
            throw new BusinessException("贴吧名称已存在");
        }
        
        // 创建贴吧
        Forum forum = new Forum();
        forum.setName(createDTO.getName());
        forum.setDescription(createDTO.getDescription());
        forum.setAvatar(createDTO.getAvatar());
        forum.setOwnerId(userId);
        forum.setCategory(createDTO.getCategory());
        forum.setMemberCount(1);
        forum.setPostCount(0);
        forum.setIsPublic(createDTO.getIsPublic());
        forum.setStatus(1);
        
        forumMapper.insert(forum);
        
        // 添加创建者为吧主
        ForumMember ownerMember = new ForumMember();
        ownerMember.setForumId(forum.getId());
        ownerMember.setUserId(userId);
        ownerMember.setRole(2); // 吧主
        ownerMember.setJoinTime(LocalDateTime.now());
        ownerMember.setStatus(1);
        
        forumMemberMapper.insert(ownerMember);
        
        return forum;
    }
    
    @Override
    public boolean joinForum(Long userId, Long forumId) {
        // 检查贴吧是否存在
        Forum forum = getForumById(forumId);
        
        // 检查是否已经是成员
        if (isMember(userId, forumId)) {
            throw new BusinessException("已经是贴吧成员");
        }
        
        // 如果是私密贴吧，需要验证权限（这里简化处理）
        if (forum.getIsPublic() == 0) {
            throw new BusinessException("私密贴吧不允许直接加入");
        }
        
        // 添加成员
        ForumMember member = new ForumMember();
        member.setForumId(forumId);
        member.setUserId(userId);
        member.setRole(0); // 普通成员
        member.setJoinTime(LocalDateTime.now());
        member.setStatus(1);
        
        forumMemberMapper.insert(member);
        
        // 更新成员数
        forum.setMemberCount(forum.getMemberCount() + 1);
        forumMapper.updateById(forum);
        
        return true;
    }
    
    @Override
    public boolean leaveForum(Long userId, Long forumId) {
        // 检查是否为成员
        ForumMember member = forumMemberMapper.selectOne(new QueryWrapper<ForumMember>()
                .eq("forum_id", forumId)
                .eq("user_id", userId));
        
        if (member == null) {
            throw new BusinessException("不是贴吧成员");
        }
        
        // 吧主不能退出自己的贴吧
        if (member.getRole() == 2) {
            throw new BusinessException("吧主不能退出贴吧");
        }
        
        // 删除成员记录
        forumMemberMapper.deleteById(member.getId());
        
        // 更新成员数
        Forum forum = getForumById(forumId);
        forum.setMemberCount(Math.max(1, forum.getMemberCount() - 1));
        forumMapper.updateById(forum);
        
        return true;
    }
    
    @Override
    public Page<Forum> getForums(ForumQueryDTO queryDTO) {
        Page<Forum> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        QueryWrapper<Forum> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        
        // 关键词搜索
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like("name", queryDTO.getKeyword())
                    .or().like("description", queryDTO.getKeyword()));
        }
        
        // 分类筛选
        if (StringUtils.hasText(queryDTO.getCategory())) {
            wrapper.eq("category", queryDTO.getCategory());
        }
        
        // 公开性筛选
        if (queryDTO.getIsPublic() != null) {
            wrapper.eq("is_public", queryDTO.getIsPublic());
        }
        
        // 吧主筛选
        if (queryDTO.getOwnerId() != null) {
            wrapper.eq("owner_id", queryDTO.getOwnerId());
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc("created_at");
        
        return forumMapper.selectPage(page, wrapper);
    }
    
    @Override
    public Forum getForumById(Long forumId) {
        Forum forum = forumMapper.selectById(forumId);
        
        if (forum == null || forum.getStatus() == 0) {
            throw new BusinessException("贴吧不存在或已被禁用");
        }
        
        return forum;
    }
    
    @Override
    public Page<ForumMember> getForumMembers(Long forumId, Integer page, Integer size) {
        Page<ForumMember> memberPage = new Page<>(page, size);
        
        QueryWrapper<ForumMember> wrapper = new QueryWrapper<>();
        wrapper.eq("forum_id", forumId);
        wrapper.orderByDesc("role").orderByAsc("join_time");
        
        return forumMemberMapper.selectPage(memberPage, wrapper);
    }
    
    @Override
    public boolean isMember(Long userId, Long forumId) {
        ForumMember member = forumMemberMapper.selectOne(new QueryWrapper<ForumMember>()
                .eq("forum_id", forumId)
                .eq("user_id", userId));
        
        return member != null;
    }
    
    @Override
    public Integer getUserRole(Long userId, Long forumId) {
        ForumMember member = forumMemberMapper.selectOne(new QueryWrapper<ForumMember>()
                .eq("forum_id", forumId)
                .eq("user_id", userId));
        
        return member != null ? member.getRole() : null;
    }
    
    @Override
    public boolean setUserRole(Long operatorId, Long forumId, Long userId, Integer role) {
        // 检查操作者权限
        Integer operatorRole = getUserRole(operatorId, forumId);
        if (operatorRole == null || operatorRole < 1) {
            throw new BusinessException("无权限操作");
        }
        
        // 不能设置比自己高的权限
        if (role >= operatorRole) {
            throw new BusinessException("不能设置比自己高或相等的权限");
        }
        
        // 获取目标用户
        ForumMember member = forumMemberMapper.selectOne(new QueryWrapper<ForumMember>()
                .eq("forum_id", forumId)
                .eq("user_id", userId));
        
        if (member == null) {
            throw new BusinessException("用户不是贴吧成员");
        }
        
        // 不能操作比自己权限高的用户
        if (member.getRole() >= operatorRole) {
            throw new BusinessException("不能操作权限高于自己的用户");
        }
        
        // 更新角色
        member.setRole(role);
        forumMemberMapper.updateById(member);
        
        return true;
    }
    
    @Override
    public ForumPost createPost(Long userId, CreateForumPostDTO postDTO) {
        // 检查用户是否为贴吧成员
        if (!isMember(userId, postDTO.getForumId())) {
            throw new BusinessException("不是贴吧成员，无法发帖");
        }
        
        // 创建帖子
        ForumPost post = new ForumPost();
        post.setForumId(postDTO.getForumId());
        post.setUserId(userId);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCategory(postDTO.getCategory());
        post.setViewCount(0);
        post.setReplyCount(0);
        post.setLikeCount(0);
        post.setIsPinned(0);
        post.setIsEssence(0);
        post.setStatus(1);
        post.setLastReplyTime(LocalDateTime.now());
        
        // 处理图片列表
        if (postDTO.getImages() != null && !postDTO.getImages().isEmpty()) {
            try {
                String imagesJson = objectMapper.writeValueAsString(postDTO.getImages());
                post.setImages(imagesJson);
            } catch (JsonProcessingException e) {
                throw new BusinessException("图片信息处理失败");
            }
        }
        
        forumPostMapper.insert(post);
        
        // 更新贴吧帖子数
        Forum forum = getForumById(postDTO.getForumId());
        forum.setPostCount(forum.getPostCount() + 1);
        forumMapper.updateById(forum);
        
        return post;
    }
    
    @Override
    public boolean deletePost(Long userId, Long postId) {
        ForumPost post = forumPostMapper.selectById(postId);
        
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在");
        }
        
        // 检查权限：帖子作者或管理员以上权限
        Integer userRole = getUserRole(userId, post.getForumId());
        if (!post.getUserId().equals(userId) && (userRole == null || userRole < 1)) {
            throw new BusinessException("无权限删除帖子");
        }
        
        // 软删除帖子
        post.setStatus(0);
        forumPostMapper.updateById(post);
        
        // 更新贴吧帖子数
        Forum forum = getForumById(post.getForumId());
        forum.setPostCount(Math.max(0, forum.getPostCount() - 1));
        forumMapper.updateById(forum);
        
        return true;
    }
    
    @Override
    public Page<ForumPost> getPosts(ForumPostQueryDTO queryDTO) {
        Page<ForumPost> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        QueryWrapper<ForumPost> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        
        // 贴吧筛选
        if (queryDTO.getForumId() != null) {
            wrapper.eq("forum_id", queryDTO.getForumId());
        }
        
        // 关键词搜索
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like("title", queryDTO.getKeyword())
                    .or().like("content", queryDTO.getKeyword()));
        }
        
        // 分类筛选
        if (StringUtils.hasText(queryDTO.getCategory())) {
            wrapper.eq("category", queryDTO.getCategory());
        }
        
        // 用户筛选
        if (queryDTO.getUserId() != null) {
            wrapper.eq("user_id", queryDTO.getUserId());
        }
        
        // 置顶筛选
        if (queryDTO.getIsPinned() != null) {
            wrapper.eq("is_pinned", queryDTO.getIsPinned());
        }
        
        // 精华筛选
        if (queryDTO.getIsEssence() != null) {
            wrapper.eq("is_essence", queryDTO.getIsEssence());
        }
        
        // 排序
        String orderBy = queryDTO.getOrderBy();
        String direction = queryDTO.getOrderDirection();
        
        if ("desc".equalsIgnoreCase(direction)) {
            wrapper.orderByDesc("is_pinned"); // 置顶帖子优先
            switch (orderBy) {
                case "create_time" -> wrapper.orderByDesc("created_at");
                case "view_count" -> wrapper.orderByDesc("view_count");
                case "reply_count" -> wrapper.orderByDesc("reply_count");
                default -> wrapper.orderByDesc("last_reply_time");
            }
        } else {
            switch (orderBy) {
                case "create_time" -> wrapper.orderByAsc("created_at");
                case "view_count" -> wrapper.orderByAsc("view_count");
                case "reply_count" -> wrapper.orderByAsc("reply_count");
                default -> wrapper.orderByAsc("last_reply_time");
            }
        }
        
        return forumPostMapper.selectPage(page, wrapper);
    }
    
    @Override
    public ForumPost getPostById(Long postId) {
        ForumPost post = forumPostMapper.selectById(postId);
        
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在");
        }
        
        // 增加浏览数
        post.setViewCount(post.getViewCount() + 1);
        forumPostMapper.updateById(post);
        
        return post;
    }
    
    @Override
    public boolean pinPost(Long operatorId, Long postId, boolean pin) {
        ForumPost post = forumPostMapper.selectById(postId);
        
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在");
        }
        
        // 检查权限：管理员以上
        Integer userRole = getUserRole(operatorId, post.getForumId());
        if (userRole == null || userRole < 1) {
            throw new BusinessException("无权限操作");
        }
        
        post.setIsPinned(pin ? 1 : 0);
        forumPostMapper.updateById(post);
        
        return true;
    }
    
    @Override
    public boolean setEssencePost(Long operatorId, Long postId, boolean essence) {
        ForumPost post = forumPostMapper.selectById(postId);
        
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在");
        }
        
        // 检查权限：管理员以上
        Integer userRole = getUserRole(operatorId, post.getForumId());
        if (userRole == null || userRole < 1) {
            throw new BusinessException("无权限操作");
        }
        
        post.setIsEssence(essence ? 1 : 0);
        forumPostMapper.updateById(post);
        
        return true;
    }
    
    @Override
    public boolean likePost(Long userId, Long postId) {
        // 检查帖子是否存在
        ForumPost post = forumPostMapper.selectById(postId);
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在");
        }
        
        // 检查是否已经点赞
        ForumPostLike existingLike = forumPostLikeMapper.selectOne(new QueryWrapper<ForumPostLike>()
                .eq("post_id", postId)
                .eq("user_id", userId));
        
        if (existingLike != null) {
            // 取消点赞
            forumPostLikeMapper.deleteById(existingLike.getId());
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
            forumPostMapper.updateById(post);
            return false;
        } else {
            // 添加点赞
            ForumPostLike like = new ForumPostLike();
            like.setPostId(postId);
            like.setUserId(userId);
            forumPostLikeMapper.insert(like);
            
            post.setLikeCount(post.getLikeCount() + 1);
            forumPostMapper.updateById(post);
            return true;
        }
    }
    
    @Override
    public ForumReply replyPost(Long userId, ForumReplyDTO replyDTO) {
        // 检查帖子是否存在
        ForumPost post = forumPostMapper.selectById(replyDTO.getPostId());
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在");
        }
        
        // 检查用户是否为贴吧成员
        if (!isMember(userId, post.getForumId())) {
            throw new BusinessException("不是贴吧成员，无法回复");
        }
        
        // 计算楼层号
        Long maxFloorLong = forumReplyMapper.selectCount(new QueryWrapper<ForumReply>()
                .eq("post_id", replyDTO.getPostId())
                .eq("status", 1));
        int maxFloor = maxFloorLong.intValue();
        
        // 创建回复
        ForumReply reply = new ForumReply();
        reply.setPostId(replyDTO.getPostId());
        reply.setUserId(userId);
        reply.setContent(replyDTO.getContent());
        reply.setReplyToUserId(replyDTO.getReplyToUserId());
        reply.setReplyToReplyId(replyDTO.getReplyToReplyId());
        reply.setFloorNumber(maxFloor + 1);
        reply.setLikeCount(0);
        reply.setStatus(1);
        
        // 处理图片列表
        if (replyDTO.getImages() != null && !replyDTO.getImages().isEmpty()) {
            try {
                String imagesJson = objectMapper.writeValueAsString(replyDTO.getImages());
                reply.setImages(imagesJson);
            } catch (JsonProcessingException e) {
                throw new BusinessException("图片信息处理失败");
            }
        }
        
        forumReplyMapper.insert(reply);
        
        // 更新帖子回复数和最后回复时间
        post.setReplyCount(post.getReplyCount() + 1);
        post.setLastReplyTime(LocalDateTime.now());
        forumPostMapper.updateById(post);
        
        return reply;
    }
    
    @Override
    public boolean deleteReply(Long userId, Long replyId) {
        ForumReply reply = forumReplyMapper.selectById(replyId);
        
        if (reply == null || reply.getStatus() == 0) {
            throw new BusinessException("回复不存在");
        }
        
        // 获取帖子信息
        ForumPost post = forumPostMapper.selectById(reply.getPostId());
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        
        // 检查权限：回复作者或管理员以上权限
        Integer userRole = getUserRole(userId, post.getForumId());
        if (!reply.getUserId().equals(userId) && (userRole == null || userRole < 1)) {
            throw new BusinessException("无权限删除回复");
        }
        
        // 软删除回复
        reply.setStatus(0);
        forumReplyMapper.updateById(reply);
        
        // 更新帖子回复数
        post.setReplyCount(Math.max(0, post.getReplyCount() - 1));
        forumPostMapper.updateById(post);
        
        return true;
    }
    
    @Override
    public Page<ForumReply> getPostReplies(Long postId, Integer page, Integer size) {
        Page<ForumReply> replyPage = new Page<>(page, size);
        
        QueryWrapper<ForumReply> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId);
        wrapper.eq("status", 1);
        wrapper.orderByAsc("floor_number");
        
        return forumReplyMapper.selectPage(replyPage, wrapper);
    }
    
    @Override
    public boolean likeReply(Long userId, Long replyId) {
        // 检查回复是否存在
        ForumReply reply = forumReplyMapper.selectById(replyId);
        if (reply == null || reply.getStatus() == 0) {
            throw new BusinessException("回复不存在");
        }
        
        // 检查是否已经点赞
        ForumReplyLike existingLike = forumReplyLikeMapper.selectOne(new QueryWrapper<ForumReplyLike>()
                .eq("reply_id", replyId)
                .eq("user_id", userId));
        
        if (existingLike != null) {
            // 取消点赞
            forumReplyLikeMapper.deleteById(existingLike.getId());
            reply.setLikeCount(Math.max(0, reply.getLikeCount() - 1));
            forumReplyMapper.updateById(reply);
            return false;
        } else {
            // 添加点赞
            ForumReplyLike like = new ForumReplyLike();
            like.setReplyId(replyId);
            like.setUserId(userId);
            forumReplyLikeMapper.insert(like);
            
            reply.setLikeCount(reply.getLikeCount() + 1);
            forumReplyMapper.updateById(reply);
            return true;
        }
    }
    
    @Override
    public boolean isPostLikedByUser(Long userId, Long postId) {
        ForumPostLike like = forumPostLikeMapper.selectOne(new QueryWrapper<ForumPostLike>()
                .eq("post_id", postId)
                .eq("user_id", userId));
        
        return like != null;
    }
    
    @Override
    public boolean isReplyLikedByUser(Long userId, Long replyId) {
        ForumReplyLike like = forumReplyLikeMapper.selectOne(new QueryWrapper<ForumReplyLike>()
                .eq("reply_id", replyId)
                .eq("user_id", userId));
        
        return like != null;
    }
}