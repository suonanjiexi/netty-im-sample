package com.example.nettyim.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.dto.CreatePaymentOrderDTO;
import com.example.nettyim.dto.Result;
import com.example.nettyim.entity.PaymentMethod;
import com.example.nettyim.entity.PaymentOrder;
import com.example.nettyim.service.PaymentService;
import com.example.nettyim.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 支付系统控制器
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取可用支付方式
     */
    @GetMapping("/methods")
    public Result<List<PaymentMethod>> getPaymentMethods() {
        List<PaymentMethod> methods = paymentService.getAvailablePaymentMethods();
        return Result.success(methods);
    }
    
    /**
     * 创建支付订单
     */
    @PostMapping("/create-order")
    public Result<Map<String, Object>> createPaymentOrder(@Valid @RequestBody CreatePaymentOrderDTO dto,
                                                          HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        try {
            // 获取支付方式
            PaymentMethod paymentMethod = paymentService.getPaymentMethodByCode(dto.getPaymentMethodCode());
            if (paymentMethod == null) {
                return Result.error("支付方式不存在");
            }
            
            // 创建支付订单
            PaymentOrder order = paymentService.createPaymentOrder(
                userId, dto.getProductType(), dto.getProductId(), 
                dto.getProductName(), dto.getAmount(), paymentMethod.getId()
            );
            
            if (order == null) {
                return Result.error("创建订单失败");
            }
            
            // 根据支付方式生成支付参数
            String paymentData = null;
            switch (dto.getPaymentMethodCode()) {
                case "WECHAT_PAY":
                    paymentData = paymentService.createWeChatPayOrder(order);
                    break;
                case "ALIPAY":
                    paymentData = paymentService.createAlipayOrder(order);
                    break;
                case "BANK_CARD":
                case "UNION_PAY":
                    paymentData = paymentService.createBankCardPayOrder(order);
                    break;
                case "WALLET_PAY":
                    // 余额支付直接返回订单信息
                    paymentData = order.getOrderNo();
                    break;
                default:
                    return Result.error("不支持的支付方式");
            }
            
            Map<String, Object> result = Map.of(
                "orderNo", order.getOrderNo(),
                "amount", order.getFinalAmount(),
                "paymentMethod", paymentMethod.getMethodName(),
                "paymentData", paymentData != null ? paymentData : ""
            );
            
            return Result.success(result);
            
        } catch (Exception e) {
            return Result.error("创建订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/order/{orderNo}")
    public Result<PaymentOrder> getOrderDetail(@PathVariable String orderNo,
                                              HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        PaymentOrder order = paymentService.getPaymentOrder(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        // 检查订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权访问此订单");
        }
        
        return Result.success(order);
    }
    
    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderNo}")
    public Result<Boolean> cancelOrder(@PathVariable String orderNo,
                                      HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        PaymentOrder order = paymentService.getPaymentOrder(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作此订单");
        }
        
        Boolean success = paymentService.cancelPaymentOrder(orderNo);
        return success ? Result.success(true) : Result.error("取消订单失败");
    }
    
    /**
     * 申请退款
     */
    @PostMapping("/refund/{orderNo}")
    public Result<Boolean> requestRefund(@PathVariable String orderNo,
                                        @RequestParam String refundReason,
                                        HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        PaymentOrder order = paymentService.getPaymentOrder(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作此订单");
        }
        
        Boolean success = paymentService.requestRefund(orderNo, order.getFinalAmount(), refundReason);
        return success ? Result.success(true) : Result.error("申请退款失败");
    }
    
    /**
     * 获取用户订单列表
     */
    @GetMapping("/my-orders")
    public Result<IPage<PaymentOrder>> getMyOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Page<PaymentOrder> pageParam = new Page<>(page, size);
        IPage<PaymentOrder> result = paymentService.getUserPaymentOrders(userId, pageParam);
        
        return Result.success(result);
    }
    
    /**
     * 支付成功回调（微信支付）
     */
    @PostMapping("/callback/wechat")
    public String wechatPayCallback(@RequestBody String callbackData) {
        try {
            // 解析微信支付回调数据
            // 这里需要根据微信支付的实际回调格式来解析
            // 示例：假设从callbackData中解析出orderNo和tradeNo
            String orderNo = ""; // 从callbackData解析
            String tradeNo = ""; // 从callbackData解析
            
            Boolean success = paymentService.handlePaymentSuccess(orderNo, tradeNo, callbackData);
            
            if (success) {
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
            } else {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }
        } catch (Exception e) {
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }
    
    /**
     * 支付成功回调（支付宝）
     */
    @PostMapping("/callback/alipay")
    public String alipayCallback(@RequestParam Map<String, String> params) {
        try {
            // 解析支付宝回调数据
            String orderNo = params.get("out_trade_no");
            String tradeNo = params.get("trade_no");
            
            Boolean success = paymentService.handlePaymentSuccess(orderNo, tradeNo, params.toString());
            
            return success ? "success" : "fail";
        } catch (Exception e) {
            return "fail";
        }
    }
    
    /**
     * 查询订单支付状态
     */
    @GetMapping("/status/{orderNo}")
    public Result<Map<String, Object>> getPaymentStatus(@PathVariable String orderNo,
                                                        HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        PaymentOrder order = paymentService.getPaymentOrder(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权访问此订单");
        }
        
        Map<String, Object> status = Map.of(
            "orderNo", order.getOrderNo(),
            "status", order.getPaymentStatus(),
            "statusText", getStatusText(order.getPaymentStatus()),
            "amount", order.getFinalAmount(),
            "paidTime", order.getPaidTime()
        );
        
        return Result.success(status);
    }
    
    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "支付中";
            case 2: return "支付成功";
            case 3: return "支付失败";
            case 4: return "已退款";
            case 5: return "已取消";
            default: return "未知状态";
        }
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