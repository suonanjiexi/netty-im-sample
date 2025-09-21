package com.example.nettyim.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.dto.Result;
import com.example.nettyim.dto.WalletOperationDTO;
import com.example.nettyim.entity.UserWallet;
import com.example.nettyim.entity.WalletTransaction;
import com.example.nettyim.service.WalletService;
import com.example.nettyim.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 钱包控制器
 */
@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    
    @Autowired
    private WalletService walletService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取用户钱包信息
     */
    @GetMapping("/info")
    public Result<UserWallet> getWalletInfo(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        UserWallet wallet = walletService.getUserWallet(userId);
        return wallet != null ? Result.success(wallet) : Result.error("钱包不存在");
    }
    
    /**
     * 获取钱包余额
     */
    @GetMapping("/balance")
    public Result<Map<String, Object>> getBalance(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        UserWallet wallet = walletService.getUserWallet(userId);
        if (wallet == null) {
            return Result.error("钱包不存在");
        }
        
        Map<String, Object> balanceInfo = Map.of(
            "balance", wallet.getBalance(),
            "frozenAmount", wallet.getFrozenAmount(),
            "availableBalance", wallet.getAvailableBalance(),
            "totalRecharge", wallet.getTotalRecharge(),
            "totalConsumption", wallet.getTotalConsumption()
        );
        
        return Result.success(balanceInfo);
    }
    
    /**
     * 钱包操作（充值、消费、转账）
     */
    @PostMapping("/operation")
    public Result<Boolean> walletOperation(@Valid @RequestBody WalletOperationDTO dto,
                                          HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        try {
            Boolean success = false;
            
            switch (dto.getOperationType().toUpperCase()) {
                case "RECHARGE":
                    success = walletService.recharge(userId, dto.getAmount(), null, dto.getDescription());
                    break;
                    
                case "CONSUME":
                    // 验证支付密码
                    if (!walletService.verifyPaymentPassword(userId, dto.getPaymentPassword())) {
                        return Result.error("支付密码错误");
                    }
                    success = walletService.consume(userId, dto.getAmount(), null, dto.getDescription());
                    break;
                    
                case "TRANSFER":
                    if (dto.getTargetUserId() == null) {
                        return Result.error("转账目标用户不能为空");
                    }
                    // 验证支付密码
                    if (!walletService.verifyPaymentPassword(userId, dto.getPaymentPassword())) {
                        return Result.error("支付密码错误");
                    }
                    // 转出
                    success = walletService.transferOut(userId, dto.getAmount(), null, dto.getDescription());
                    if (success) {
                        // 转入
                        walletService.transferIn(dto.getTargetUserId(), dto.getAmount(), null, "来自用户转账");
                    }
                    break;
                    
                default:
                    return Result.error("不支持的操作类型");
            }
            
            return success ? Result.success(true) : Result.error("操作失败");
            
        } catch (Exception e) {
            return Result.error("操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 设置支付密码
     */
    @PostMapping("/set-payment-password")
    public Result<Boolean> setPaymentPassword(@RequestParam String password,
                                             HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        if (password == null || password.length() < 6) {
            return Result.error("支付密码长度不能少于6位");
        }
        
        Boolean success = walletService.setPaymentPassword(userId, password);
        return success ? Result.success(true) : Result.error("设置支付密码失败");
    }
    
    /**
     * 验证支付密码
     */
    @PostMapping("/verify-payment-password")
    public Result<Boolean> verifyPaymentPassword(@RequestParam String password,
                                                HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean valid = walletService.verifyPaymentPassword(userId, password);
        return Result.success(valid);
    }
    
    /**
     * 获取交易记录
     */
    @GetMapping("/transactions")
    public Result<IPage<WalletTransaction>> getTransactions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Page<WalletTransaction> pageParam = new Page<>(page, size);
        IPage<WalletTransaction> result = walletService.getUserTransactions(userId, pageParam);
        
        return Result.success(result);
    }
    
    /**
     * 检查余额是否足够
     */
    @GetMapping("/check-balance")
    public Result<Boolean> checkBalance(@RequestParam BigDecimal amount,
                                       HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean enough = walletService.hasEnoughBalance(userId, amount);
        return Result.success(enough);
    }
    
    /**
     * 冻结资金
     */
    @PostMapping("/freeze")
    public Result<Boolean> freezeAmount(@RequestParam BigDecimal amount,
                                       @RequestParam String reason,
                                       HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean success = walletService.freezeAmount(userId, amount, null, reason);
        return success ? Result.success(true) : Result.error("冻结资金失败");
    }
    
    /**
     * 解冻资金
     */
    @PostMapping("/unfreeze")
    public Result<Boolean> unfreezeAmount(@RequestParam BigDecimal amount,
                                         @RequestParam String reason,
                                         HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Boolean success = walletService.unfreezeAmount(userId, amount, null, reason);
        return success ? Result.success(true) : Result.error("解冻资金失败");
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