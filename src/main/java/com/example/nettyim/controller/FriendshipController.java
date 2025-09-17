package com.example.nettyim.controller;

import com.example.nettyim.dto.AddFriendDTO;
import com.example.nettyim.dto.Result;
import com.example.nettyim.entity.Friendship;
import com.example.nettyim.entity.User;
import com.example.nettyim.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 好友控制器
 */
@RestController
@RequestMapping("/friendship")
public class FriendshipController {
    
    private final FriendshipService friendshipService;
    
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }
    
    /**
     * 添加好友
     */
    @PostMapping("/add")
    public Result<String> addFriend(@RequestParam Long userId, 
                                   @Valid @RequestBody AddFriendDTO addFriendDTO) {
        friendshipService.addFriend(userId, addFriendDTO);
        return Result.success("好友申请已发送");
    }
    
    /**
     * 处理好友申请
     */
    @PostMapping("/handle")
    public Result<String> handleFriendRequest(@RequestParam Long userId,
                                             @RequestParam Long requestId,
                                             @RequestParam Integer action,
                                             @RequestParam(required = false) String remark) {
        friendshipService.handleFriendRequest(userId, requestId, action, remark);
        String message = action == 1 ? "已同意好友申请" : "已拒绝好友申请";
        return Result.success(message);
    }
    
    /**
     * 获取好友列表
     */
    @GetMapping("/list")
    public Result<List<User>> getFriendList(@RequestParam Long userId) {
        List<User> friends = friendshipService.getFriendList(userId);
        return Result.success(friends);
    }
    
    /**
     * 获取好友申请列表
     */
    @GetMapping("/requests")
    public Result<List<Friendship>> getFriendRequestList(@RequestParam Long userId) {
        List<Friendship> requests = friendshipService.getFriendRequestList(userId);
        return Result.success(requests);
    }
    
    /**
     * 删除好友
     */
    @DeleteMapping("/delete")
    public Result<String> deleteFriend(@RequestParam Long userId, 
                                      @RequestParam Long friendId) {
        friendshipService.deleteFriend(userId, friendId);
        return Result.success("好友删除成功");
    }
    
    /**
     * 更新好友备注
     */
    @PutMapping("/remark")
    public Result<String> updateFriendRemark(@RequestParam Long userId,
                                            @RequestParam Long friendId,
                                            @RequestParam String remark) {
        friendshipService.updateFriendRemark(userId, friendId, remark);
        return Result.success("备注更新成功");
    }
    
    /**
     * 检查是否为好友
     */
    @GetMapping("/check")
    public Result<Boolean> isFriend(@RequestParam Long userId, 
                                   @RequestParam Long friendId) {
        boolean isFriend = friendshipService.isFriend(userId, friendId);
        return Result.success(isFriend);
    }
}