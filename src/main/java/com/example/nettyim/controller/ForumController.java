package com.example.nettyim.controller;

import com.example.nettyim.dto.*;
import com.example.nettyim.entity.*;
import com.example.nettyim.service.ForumService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 贴吧控制器
 */
@RestController
@RequestMapping("/forum")
public class ForumController {
    
    private final ForumService forumService;
    
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }
    
    /**
     * 创建贴吧
     */
    @PostMapping("/create")
    public Result<Forum> createForum(@RequestParam Long userId,
                                    @Valid @RequestBody CreateForumDTO createDTO) {
        Forum forum = forumService.createForum(userId, createDTO);
        return Result.success("创建贴吧成功", forum);
    }
    
    /**
     * 加入贴吧
     */
    @PostMapping("/{forumId}/join")
    public Result<String> joinForum(@RequestParam Long userId,
                                   @PathVariable Long forumId) {
        boolean success = forumService.joinForum(userId, forumId);
        return success ? Result.success("加入贴吧成功") : Result.error("加入贴吧失败");
    }
    
    /**
     * 退出贴吧
     */
    @PostMapping("/{forumId}/leave")
    public Result<String> leaveForum(@RequestParam Long userId,
                                    @PathVariable Long forumId) {
        boolean success = forumService.leaveForum(userId, forumId);
        return success ? Result.success("退出贴吧成功") : Result.error("退出贴吧失败");
    }
    
    /**
     * 分页查询贴吧列表
     */
    @GetMapping("/list")
    public Result<Page<Forum>> getForums(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) String category,
                                        @RequestParam(required = false) Integer isPublic,
                                        @RequestParam(required = false) Long ownerId) {
        ForumQueryDTO queryDTO = new ForumQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setKeyword(keyword);
        queryDTO.setCategory(category);
        queryDTO.setIsPublic(isPublic);
        queryDTO.setOwnerId(ownerId);
        
        Page<Forum> forums = forumService.getForums(queryDTO);
        return Result.success(forums);
    }
    
    /**
     * 获取贴吧详情
     */
    @GetMapping("/{forumId}")
    public Result<Forum> getForumById(@PathVariable Long forumId) {
        Forum forum = forumService.getForumById(forumId);
        return Result.success(forum);
    }
    
    /**
     * 获取贴吧成员列表
     */
    @GetMapping("/{forumId}/members")
    public Result<Page<ForumMember>> getForumMembers(@PathVariable Long forumId,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<ForumMember> members = forumService.getForumMembers(forumId, page, size);
        return Result.success(members);
    }
    
    /**
     * 检查用户是否为贴吧成员
     */
    @GetMapping("/{forumId}/member/{userId}")
    public Result<Map<String, Object>> checkMember(@PathVariable Long forumId,
                                                   @PathVariable Long userId) {
        boolean isMember = forumService.isMember(userId, forumId);
        Integer role = forumService.getUserRole(userId, forumId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("isMember", isMember);
        data.put("role", role);
        
        return Result.success(data);
    }
    
    /**
     * 设置用户角色
     */
    @PostMapping("/{forumId}/member/{userId}/role")
    public Result<String> setUserRole(@RequestParam Long operatorId,
                                     @PathVariable Long forumId,
                                     @PathVariable Long userId,
                                     @RequestParam Integer role) {
        boolean success = forumService.setUserRole(operatorId, forumId, userId, role);
        return success ? Result.success("角色设置成功") : Result.error("角色设置失败");
    }
    
    /**
     * 发布帖子
     */
    @PostMapping("/post/create")
    public Result<ForumPost> createPost(@RequestParam Long userId,
                                       @Valid @RequestBody CreateForumPostDTO postDTO) {
        ForumPost post = forumService.createPost(userId, postDTO);
        return Result.success("发帖成功", post);
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/post/{postId}")
    public Result<String> deletePost(@RequestParam Long userId,
                                    @PathVariable Long postId) {
        boolean success = forumService.deletePost(userId, postId);
        return success ? Result.success("删除帖子成功") : Result.error("删除帖子失败");
    }
    
    /**
     * 分页查询帖子列表
     */
    @GetMapping("/post/list")
    public Result<Page<ForumPost>> getPosts(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) Long forumId,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) String category,
                                           @RequestParam(required = false) Long userId,
                                           @RequestParam(required = false) Integer isPinned,
                                           @RequestParam(required = false) Integer isEssence,
                                           @RequestParam(defaultValue = "last_reply_time") String orderBy,
                                           @RequestParam(defaultValue = "desc") String orderDirection) {
        ForumPostQueryDTO queryDTO = new ForumPostQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setForumId(forumId);
        queryDTO.setKeyword(keyword);
        queryDTO.setCategory(category);
        queryDTO.setUserId(userId);
        queryDTO.setIsPinned(isPinned);
        queryDTO.setIsEssence(isEssence);
        queryDTO.setOrderBy(orderBy);
        queryDTO.setOrderDirection(orderDirection);
        
        Page<ForumPost> posts = forumService.getPosts(queryDTO);
        return Result.success(posts);
    }
    
    /**
     * 获取帖子详情
     */
    @GetMapping("/post/{postId}")
    public Result<ForumPost> getPostById(@PathVariable Long postId) {
        ForumPost post = forumService.getPostById(postId);
        return Result.success(post);
    }
    
    /**
     * 置顶/取消置顶帖子
     */
    @PostMapping("/post/{postId}/pin")
    public Result<String> pinPost(@RequestParam Long operatorId,
                                 @PathVariable Long postId,
                                 @RequestParam boolean pin) {
        boolean success = forumService.pinPost(operatorId, postId, pin);
        String message = pin ? "置顶成功" : "取消置顶成功";
        return success ? Result.success(message) : Result.error("操作失败");
    }
    
    /**
     * 设置/取消精华帖
     */
    @PostMapping("/post/{postId}/essence")
    public Result<String> setEssencePost(@RequestParam Long operatorId,
                                        @PathVariable Long postId,
                                        @RequestParam boolean essence) {
        boolean success = forumService.setEssencePost(operatorId, postId, essence);
        String message = essence ? "设为精华成功" : "取消精华成功";
        return success ? Result.success(message) : Result.error("操作失败");
    }
    
    /**
     * 点赞/取消点赞帖子
     */
    @PostMapping("/post/{postId}/like")
    public Result<Map<String, Object>> likePost(@RequestParam Long userId,
                                               @PathVariable Long postId) {
        boolean liked = forumService.likePost(userId, postId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("message", liked ? "点赞成功" : "取消点赞成功");
        
        return Result.success(data);
    }
    
    /**
     * 回复帖子
     */
    @PostMapping("/reply/create")
    public Result<ForumReply> replyPost(@RequestParam Long userId,
                                       @Valid @RequestBody ForumReplyDTO replyDTO) {
        ForumReply reply = forumService.replyPost(userId, replyDTO);
        return Result.success("回复成功", reply);
    }
    
    /**
     * 删除回复
     */
    @DeleteMapping("/reply/{replyId}")
    public Result<String> deleteReply(@RequestParam Long userId,
                                     @PathVariable Long replyId) {
        boolean success = forumService.deleteReply(userId, replyId);
        return success ? Result.success("删除回复成功") : Result.error("删除回复失败");
    }
    
    /**
     * 获取帖子回复列表
     */
    @GetMapping("/post/{postId}/replies")
    public Result<Page<ForumReply>> getPostReplies(@PathVariable Long postId,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        Page<ForumReply> replies = forumService.getPostReplies(postId, page, size);
        return Result.success(replies);
    }
    
    /**
     * 点赞/取消点赞回复
     */
    @PostMapping("/reply/{replyId}/like")
    public Result<Map<String, Object>> likeReply(@RequestParam Long userId,
                                                @PathVariable Long replyId) {
        boolean liked = forumService.likeReply(userId, replyId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("message", liked ? "点赞成功" : "取消点赞成功");
        
        return Result.success(data);
    }
    
    /**
     * 检查用户是否已点赞帖子
     */
    @GetMapping("/post/{postId}/liked")
    public Result<Boolean> isPostLikedByUser(@RequestParam Long userId,
                                            @PathVariable Long postId) {
        boolean liked = forumService.isPostLikedByUser(userId, postId);
        return Result.success(liked);
    }
    
    /**
     * 检查用户是否已点赞回复
     */
    @GetMapping("/reply/{replyId}/liked")
    public Result<Boolean> isReplyLikedByUser(@RequestParam Long userId,
                                             @PathVariable Long replyId) {
        boolean liked = forumService.isReplyLikedByUser(userId, replyId);
        return Result.success(liked);
    }
}