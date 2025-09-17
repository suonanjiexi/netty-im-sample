package com.example.nettyim.controller;

import com.example.nettyim.dto.MessageQueryDTO;
import com.example.nettyim.dto.Result;
import com.example.nettyim.dto.SendMessageDTO;
import com.example.nettyim.entity.Message;
import com.example.nettyim.service.MessageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 消息控制器
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    
    private final MessageService messageService;
    
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    
    /**
     * 发送消息
     */
    @PostMapping("/send")
    public Result<Message> sendMessage(@RequestParam Long fromUserId,
                                      @Valid @RequestBody SendMessageDTO sendMessageDTO) {
        Message message = messageService.sendMessage(fromUserId, sendMessageDTO);
        return Result.success("消息发送成功", message);
    }
    
    /**
     * 获取私聊消息历史
     */
    @PostMapping("/private/history")
    public Result<Page<Message>> getPrivateMessages(@Valid @RequestBody MessageQueryDTO queryDTO) {
        Page<Message> messages = messageService.getPrivateMessages(queryDTO);
        return Result.success(messages);
    }
    
    /**
     * 获取群聊消息历史
     */
    @PostMapping("/group/history")
    public Result<Page<Message>> getGroupMessages(@Valid @RequestBody MessageQueryDTO queryDTO) {
        Page<Message> messages = messageService.getGroupMessages(queryDTO);
        return Result.success(messages);
    }
    
    /**
     * 标记消息为已读
     */
    @PostMapping("/read")
    public Result<String> markMessageAsRead(@RequestParam Long userId,
                                           @RequestParam Long messageId) {
        messageService.markMessageAsRead(userId, messageId);
        return Result.success("消息已标记为已读");
    }
    
    /**
     * 批量标记消息为已读
     */
    @PostMapping("/read/batch")
    public Result<String> markMessagesAsRead(@RequestParam Long userId,
                                            @RequestBody List<Long> messageIds) {
        messageService.markMessagesAsRead(userId, messageIds);
        return Result.success("消息已批量标记为已读");
    }
    
    /**
     * 删除消息
     */
    @DeleteMapping("/{messageId}")
    public Result<String> deleteMessage(@PathVariable Long messageId,
                                       @RequestParam Long userId) {
        messageService.deleteMessage(userId, messageId);
        return Result.success("消息删除成功");
    }
    
    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread/count")
    public Result<Integer> getUnreadMessageCount(@RequestParam Long userId,
                                                @RequestParam Long targetId,
                                                @RequestParam Integer conversationType) {
        int count = messageService.getUnreadMessageCount(userId, targetId, conversationType);
        return Result.success(count);
    }
    
    /**
     * 获取消息详情
     */
    @GetMapping("/{messageId}")
    public Result<Message> getMessageById(@PathVariable Long messageId) {
        Message message = messageService.getMessageById(messageId);
        return Result.success(message);
    }
    
    /**
     * 撤回消息
     */
    @PostMapping("/{messageId}/recall")
    public Result<String> recallMessage(@PathVariable Long messageId,
                                       @RequestParam Long userId) {
        messageService.recallMessage(userId, messageId);
        return Result.success("消息撤回成功");
    }
}