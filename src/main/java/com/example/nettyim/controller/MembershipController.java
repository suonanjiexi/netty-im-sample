package com.example.nettyim.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.dto.MembershipOperationDTO;
import com.example.nettyim.dto.Result;
import com.example.nettyim.entity.MembershipLevel;
import com.example.nettyim.entity.MembershipPoint;
import com.example.nettyim.entity.UserMembership;
import com.example.nettyim.service.MembershipService;
import com.example.nettyim.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 会员系统控制器
 */
@RestController
@RequestMapping("/api/membership")
public class MembershipController {
    
    @Autowired
    private MembershipService membershipService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取所有会员等级
     */
    @GetMapping("/levels")
    public Result<List<MembershipLevel>> getAllLevels() {
        List<MembershipLevel> levels = membershipService.getAllActiveLevels();
        return Result.success(levels);
    }
    
    /**
     * 获取用户会员信息
     */
    @GetMapping("/my-membership")
    public Result<UserMembership> getMyMembership(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        UserMembership membership = membershipService.getUserMembership(userId);
        return Result.success(membership);
    }
    
    /**
     * 获取用户当前积分
     */
    @GetMapping("/my-points")
    public Result<Integer> getMyPoints(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Integer points = membershipService.getUserPoints(userId);
        return Result.success(points);
    }
    
    /**
     * 获取用户积分记录
     */
    @GetMapping("/points-history")
    public Result<IPage<MembershipPoint>> getPointsHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Page<MembershipPoint> pageParam = new Page<>(page, size);
        IPage<MembershipPoint> result = membershipService.getUserPointsHistory(userId, pageParam);
        
        return Result.success(result);
    }
    
    /**
     * 激活试用会员
     */
    @PostMapping("/trial")
    public Result<Boolean> activateTrial(@Valid @RequestBody MembershipOperationDTO dto,
                                        HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        if (dto.getLevelId() == null) {
            return Result.error("会员等级ID不能为空");
        }
        
        Boolean success = membershipService.activateTrialMembership(userId, dto.getLevelId());
        return success ? Result.success(true) : Result.error("激活试用失败");
    }
    
    /**
     * 检查用户权限
     */
    @GetMapping("/check-permission")
    public Result<Boolean> checkPermission(@RequestParam String permission,
                                          HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean hasPermission = membershipService.hasPermission(userId, permission);
        return Result.success(hasPermission);
    }
    
    /**
     * 获取用户会员权益
     */
    @GetMapping("/my-benefits")
    public Result<List<String>> getMyBenefits(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<String> benefits = membershipService.getUserBenefits(userId);
        return Result.success(benefits);
    }
    
    /**
     * 生成推荐码
     */
    @PostMapping("/generate-referral-code")
    public Result<String> generateReferralCode(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        String referralCode = membershipService.generateReferralCode(userId);
        return Result.success(referralCode);
    }
    
    /**
     * 使用积分
     */
    @PostMapping("/use-points")
    public Result<Boolean> usePoints(@RequestParam Integer points,
                                    @RequestParam String description,
                                    HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        if (points <= 0) {
            return Result.error("积分数量必须大于0");
        }
        
        Boolean success = membershipService.deductUserPoints(userId, points, "USE", null, description);
        return success ? Result.success(true) : Result.error("积分不足或操作失败");
    }
    
    /**
     * 获取会员等级详情
     */
    @GetMapping("/level/{levelId}")
    public Result<MembershipLevel> getLevelDetail(@PathVariable Long levelId) {
        MembershipLevel level = membershipService.getLevelById(levelId);
        return level != null ? Result.success(level) : Result.error("会员等级不存在");
    }
    
    /**
     * 获取用户会员统计信息
     */
    @GetMapping("/my-stats")
    public Result<Map<String, Object>> getMyStats(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        UserMembership membership = membershipService.getUserMembership(userId);
        if (membership == null) {
            return Result.error("会员信息不存在");
        }
        
        Map<String, Object> stats = Map.of(
            "totalPaid", membership.getTotalPaid(),
            "pointsEarned", membership.getPointsEarned(),
            "pointsUsed", membership.getPointsUsed(),
            "currentPoints", membership.getCurrentPoints(),
            "referralCode", membership.getReferralCode()
        );
        
        return Result.success(stats);
    }
    
    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtils.getUserIdFromToken(token);
        }
        return null;
    }
}