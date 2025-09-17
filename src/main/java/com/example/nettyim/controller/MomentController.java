package com.example.nettyim.controller;

import com.example.nettyim.dto.MomentCommentDTO;
import com.example.nettyim.dto.MomentQueryDTO;
import com.example.nettyim.dto.PublishMomentDTO;
import com.example.nettyim.dto.Result;
import com.example.nettyim.entity.Moment;
import com.example.nettyim.entity.MomentComment;
import com.example.nettyim.service.MomentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 朋友圈控制器
 */
@RestController
@RequestMapping("/moment")
public class MomentController {
    
    private final MomentService momentService;
    
    public MomentController(MomentService momentService) {
        this.momentService = momentService;
    }
    
    /**
     * 发布朋友圈动态
     */
    @PostMapping("/publish")
    public Result<Moment> publishMoment(@RequestParam Long userId,
                                       @Valid @RequestBody PublishMomentDTO publishDTO) {
        Moment moment = momentService.publishMoment(userId, publishDTO);
        return Result.success("发布成功", moment);
    }
    
    /**
     * 删除朋友圈动态
     */
    @DeleteMapping("/{momentId}")
    public Result<String> deleteMoment(@RequestParam Long userId,
                                      @PathVariable Long momentId) {
        momentService.deleteMoment(userId, momentId);
        return Result.success("删除成功");
    }
    
    /**
     * 分页查询朋友圈动态
     */
    @GetMapping("/list")
    public Result<Page<Moment>> getMoments(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long userId,
                                          @RequestParam(required = false) Integer visibility) {
        MomentQueryDTO queryDTO = new MomentQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setUserId(userId);
        queryDTO.setVisibility(visibility);
        
        Page<Moment> moments = momentService.getMoments(queryDTO);
        return Result.success(moments);
    }
    
    /**
     * 获取动态详情
     */
    @GetMapping("/{momentId}")
    public Result<Moment> getMomentById(@PathVariable Long momentId) {
        Moment moment = momentService.getMomentById(momentId);
        return Result.success(moment);
    }
    
    /**
     * 点赞/取消点赞动态
     */
    @PostMapping("/{momentId}/like")
    public Result<Map<String, Object>> likeMoment(@RequestParam Long userId,
                                                 @PathVariable Long momentId) {
        boolean liked = momentService.likeMoment(userId, momentId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("message", liked ? "点赞成功" : "取消点赞成功");
        
        return Result.success(data);
    }
    
    /**
     * 评论动态
     */
    @PostMapping("/comment")
    public Result<MomentComment> commentMoment(@RequestParam Long userId,
                                              @Valid @RequestBody MomentCommentDTO commentDTO) {
        MomentComment comment = momentService.commentMoment(userId, commentDTO);
        return Result.success("评论成功", comment);
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/comment/{commentId}")
    public Result<String> deleteComment(@RequestParam Long userId,
                                       @PathVariable Long commentId) {
        momentService.deleteComment(userId, commentId);
        return Result.success("删除评论成功");
    }
    
    /**
     * 获取动态的评论列表
     */
    @GetMapping("/{momentId}/comments")
    public Result<Page<MomentComment>> getMomentComments(@PathVariable Long momentId,
                                                        @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        Page<MomentComment> comments = momentService.getMomentComments(momentId, page, size);
        return Result.success(comments);
    }
    
    /**
     * 检查用户是否已点赞动态
     */
    @GetMapping("/{momentId}/liked")
    public Result<Boolean> isLikedByUser(@RequestParam Long userId,
                                        @PathVariable Long momentId) {
        boolean liked = momentService.isLikedByUser(userId, momentId);
        return Result.success(liked);
    }
}