package com.example.nettyim.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.admin.service.PaymentManagementService;
import com.example.nettyim.entity.PaymentOrder;
import com.example.nettyim.entity.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付订单管理控制器
 */
@RestController
@RequestMapping("/admin/api/payments")
public class PaymentController {
    
    @Autowired
    private PaymentManagementService paymentManagementService;
    
    /**
     * 分页查询支付订单列表
     */
    @GetMapping("/orders")
    public Map<String, Object> pagePaymentOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer paymentStatus) {
        
        Page<PaymentOrder> pageRequest = new Page<>(page, size);
        IPage<PaymentOrder> pageResult = paymentManagementService.pagePaymentOrders(pageRequest, orderNo, userId, paymentStatus);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取支付订单详情
     */
    @GetMapping("/orders/{id}")
    public Map<String, Object> getPaymentOrderById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        PaymentOrder order = paymentManagementService.getPaymentOrderById(id);
        if (order != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", order);
        } else {
            result.put("success", false);
            result.put("message", "支付订单不存在");
        }
        return result;
    }
    
    /**
     * 处理退款申请
     */
    @PostMapping("/orders/{id}/refund")
    public Map<String, Object> processRefund(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        String refundReason = (String) params.get("refundReason");
        boolean success = paymentManagementService.processRefund(id, refundReason);
        
        if (success) {
            result.put("success", true);
            result.put("message", "退款申请处理成功");
        } else {
            result.put("success", false);
            result.put("message", "退款申请处理失败");
        }
        return result;
    }
    
    /**
     * 分页查询支付方式列表
     */
    @GetMapping("/methods")
    public Map<String, Object> pagePaymentMethods(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<PaymentMethod> pageRequest = new Page<>(page, size);
        IPage<PaymentMethod> pageResult = paymentManagementService.pagePaymentMethods(pageRequest);
        
        return getPageResultMap(pageResult);
    }
    
    /**
     * 获取支付方式详情
     */
    @GetMapping("/methods/{id}")
    public Map<String, Object> getPaymentMethodById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        PaymentMethod method = paymentManagementService.getPaymentMethodById(id);
        if (method != null) {
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", method);
        } else {
            result.put("success", false);
            result.put("message", "支付方式不存在");
        }
        return result;
    }
    
    /**
     * 创建支付方式
     */
    @PostMapping("/methods")
    public Map<String, Object> createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = paymentManagementService.createPaymentMethod(paymentMethod);
        if (success) {
            result.put("success", true);
            result.put("message", "创建成功");
        } else {
            result.put("success", false);
            result.put("message", "创建失败");
        }
        return result;
    }
    
    /**
     * 更新支付方式
     */
    @PutMapping("/methods/{id}")
    public Map<String, Object> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethod paymentMethod) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = paymentManagementService.updatePaymentMethod(id, paymentMethod);
        if (success) {
            result.put("success", true);
            result.put("message", "更新成功");
        } else {
            result.put("success", false);
            result.put("message", "更新失败");
        }
        return result;
    }
    
    /**
     * 删除支付方式
     */
    @DeleteMapping("/methods/{id}")
    public Map<String, Object> deletePaymentMethod(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = paymentManagementService.deletePaymentMethod(id);
        if (success) {
            result.put("success", true);
            result.put("message", "删除成功");
        } else {
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }
    
    /**
     * 更新支付方式状态
     */
    @PutMapping("/methods/{id}/status")
    public Map<String, Object> updatePaymentMethodStatus(@PathVariable Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = paymentManagementService.updatePaymentMethodStatus(id, status);
        if (success) {
            result.put("success", true);
            result.put("message", "状态更新成功");
        } else {
            result.put("success", false);
            result.put("message", "状态更新失败");
        }
        return result;
    }
    
    /**
     * 构造分页结果
     */
    private Map<String, Object> getPageResultMap(IPage<?> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "查询成功");
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        return result;
    }
}