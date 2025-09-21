package com.example.nettyim.controller;

import com.example.nettyim.dto.Result;
import com.example.nettyim.entity.*;
import com.example.nettyim.service.*;
import com.example.nettyim.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 社交功能控制器
 */
@RestController
@RequestMapping("/api/social")
public class SocialController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private CheckInService checkInService;
    
    @Autowired
    private SocialAchievementService achievementService;
    
    @Autowired
    private UserLevelService levelService;
    
    @Autowired
    private PrivacyService privacyService;
    
    @Autowired
    private AntiHarassmentService antiHarassmentService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取好友推荐
     */
    @GetMapping("/recommendations/friends")
    public Result<List<UserRecommendation>> getFriendRecommendations(
            @RequestParam(defaultValue = "20") Integer limit,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<UserRecommendation> recommendations = recommendationService.getFriendRecommendations(userId, limit);
        return Result.success(recommendations);
    }
    
    /**
     * 基于位置的好友推荐
     */
    @GetMapping("/recommendations/location")
    public Result<List<UserRecommendation>> getLocationBasedRecommendations(
            @RequestParam(defaultValue = "10.0") Double radiusKm,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<UserRecommendation> recommendations = recommendationService.getLocationBasedRecommendations(userId, radiusKm);
        return Result.success(recommendations);
    }
    
    /**
     * 基于兴趣的好友推荐
     */
    @GetMapping("/recommendations/interests")
    public Result<List<UserRecommendation>> getInterestBasedRecommendations(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<UserRecommendation> recommendations = recommendationService.getInterestBasedRecommendations(userId);
        return Result.success(recommendations);
    }
    
    /**
     * 用户签到
     */
    @PostMapping("/checkin")
    public Result<Map<String, Object>> checkIn(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        if (checkInService.isCheckedInToday(userId)) {
            return Result.error("今日已签到");
        }
        
        UserCheckIn checkIn = checkInService.checkIn(userId);
        Map<String, Object> reward = checkInService.calculateCheckInReward(checkIn.getContinuousDays());
        
        Map<String, Object> result = Map.of(
            "checkIn", checkIn,
            "reward", reward,
            "continuousDays", checkIn.getContinuousDays()
        );
        
        return Result.success(result);
    }
    
    /**
     * 获取签到统计
     */
    @GetMapping("/checkin/stats")
    public Result<Map<String, Object>> getCheckInStats(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Map<String, Object> stats = checkInService.getCheckInStats(userId);
        return Result.success(stats);
    }
    
    /**
     * 获取月度签到日历
     */
    @GetMapping("/checkin/calendar")
    public Result<Map<String, Object>> getMonthlyCheckInCalendar(
            @RequestParam(required = false) String month,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        LocalDate monthDate = month != null ? LocalDate.parse(month + "-01") : LocalDate.now();
        Map<String, Object> calendar = checkInService.getMonthlyCheckInCalendar(userId, monthDate);
        
        return Result.success(calendar);
    }
    
    /**
     * 获取用户成就列表
     */
    @GetMapping("/achievements")
    public Result<Map<String, Object>> getUserAchievements(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<UserAchievement> completed = achievementService.getUserCompletedAchievements(userId);
        List<UserAchievement> inProgress = achievementService.getUserInProgressAchievements(userId);
        Map<String, Object> stats = achievementService.getAchievementStats(userId);
        
        Map<String, Object> result = Map.of(
            "completed", completed,
            "inProgress", inProgress,
            "stats", stats
        );
        
        return Result.success(result);
    }
    
    /**
     * 获取用户等级信息
     */
    @GetMapping("/level")
    public Result<Map<String, Object>> getUserLevel(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        UserExperience userExp = levelService.getUserExperience(userId);
        Map<String, Object> stats = levelService.getLevelStats(userId);
        List<String> privileges = levelService.getUserLevelPrivileges(userId);
        
        Map<String, Object> result = Map.of(
            "experience", userExp,
            "stats", stats,
            "privileges", privileges
        );
        
        return Result.success(result);
    }
    
    /**
     * 获取经验排行榜
     */
    @GetMapping("/level/ranking")
    public Result<List<Map<String, Object>>> getExpRanking(
            @RequestParam(defaultValue = "50") Integer limit) {
        
        List<Map<String, Object>> ranking = levelService.getExpRanking(limit);
        return Result.success(ranking);
    }
    
    /**
     * 举报骚扰
     */
    @PostMapping("/report/harassment")
    public Result<Boolean> reportHarassment(
            @RequestParam Long reportedUserId,
            @RequestParam Integer reportType,
            @RequestParam(required = false) Integer contentType,
            @RequestParam(required = false) Long contentId,
            @RequestParam String description,
            @RequestParam(required = false) List<String> evidenceUrls,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean success = antiHarassmentService.submitHarassmentReport(
            userId, reportedUserId, reportType, contentType, contentId, description, evidenceUrls);
        
        return success ? Result.success(true) : Result.error("举报失败");
    }
    
    /**
     * 拉黑用户
     */
    @PostMapping("/block/{targetUserId}")
    public Result<Boolean> blockUser(@PathVariable Long targetUserId,
                                    @RequestParam(required = false) String reason,
                                    HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean success = antiHarassmentService.blockUser(userId, targetUserId, reason);
        return success ? Result.success(true) : Result.error("拉黑失败");
    }
    
    /**
     * 取消拉黑
     */
    @DeleteMapping("/block/{targetUserId}")
    public Result<Boolean> unblockUser(@PathVariable Long targetUserId,
                                      HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean success = antiHarassmentService.unblockUser(userId, targetUserId);
        return success ? Result.success(true) : Result.error("取消拉黑失败");
    }
    
    /**
     * 获取隐私设置
     */
    @GetMapping("/privacy/settings")
    public Result<List<PrivacySetting>> getPrivacySettings(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<PrivacySetting> settings = privacyService.getUserPrivacySettings(userId);
        return Result.success(settings);
    }
    
    /**
     * 更新隐私设置
     */
    @PutMapping("/privacy/settings")
    public Result<Boolean> updatePrivacySettings(
            @RequestBody Map<String, String> settings,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean success = privacyService.updatePrivacySettings(userId, settings);
        return success ? Result.success(true) : Result.error("更新失败");
    }
    
    /**
     * 获取安全概览
     */
    @GetMapping("/security/overview")
    public Result<Map<String, Object>> getSecurityOverview(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Map<String, Object> overview = securityService.getSecurityOverview(userId);
        return Result.success(overview);
    }
    
    /**
     * 获取登录设备列表
     */
    @GetMapping("/security/devices")
    public Result<List<Map<String, Object>>> getLoginDevices(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<Map<String, Object>> devices = securityService.getLoginDevices(userId);
        return Result.success(devices);
    }
    
    /**
     * 强制下线所有设备
     */
    @PostMapping("/security/logout-all")
    public Result<Boolean> forceLogoutAllDevices(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean success = securityService.forceLogoutAllDevices(userId);
        return success ? Result.success(true) : Result.error("操作失败");
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