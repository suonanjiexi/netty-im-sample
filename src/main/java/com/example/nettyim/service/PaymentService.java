package com.example.nettyim.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.PaymentMethod;
import com.example.nettyim.entity.PaymentOrder;
import com.example.nettyim.entity.UserWallet;
import com.example.nettyim.entity.WalletTransaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付服务接口
 */
public interface PaymentService {
    
    /**
     * 获取所有可用的支付方式
     */
    List<PaymentMethod> getAvailablePaymentMethods();
    
    /**
     * 根据代码获取支付方式
     */
    PaymentMethod getPaymentMethodByCode(String methodCode);
    
    /**
     * 创建支付订单
     */
    PaymentOrder createPaymentOrder(Long userId, Integer productType, Long productId, 
                                   String productName, BigDecimal amount, Long paymentMethodId);
    
    /**
     * 获取支付订单
     */
    PaymentOrder getPaymentOrder(String orderNo);
    
    /**
     * 处理支付成功回调
     */
    Boolean handlePaymentSuccess(String orderNo, String tradeNo, String callbackData);
    
    /**
     * 处理支付失败
     */
    Boolean handlePaymentFailure(String orderNo, String reason);
    
    /**
     * 取消支付订单
     */
    Boolean cancelPaymentOrder(String orderNo);
    
    /**
     * 申请退款
     */
    Boolean requestRefund(String orderNo, BigDecimal refundAmount, String refundReason);
    
    /**
     * 处理退款成功
     */
    Boolean handleRefundSuccess(String orderNo, BigDecimal refundAmount);
    
    /**
     * 获取用户支付订单列表
     */
    IPage<PaymentOrder> getUserPaymentOrders(Long userId, Page<PaymentOrder> page);
    
    /**
     * 微信支付下单
     */
    String createWeChatPayOrder(PaymentOrder order);
    
    /**
     * 支付宝支付下单
     */
    String createAlipayOrder(PaymentOrder order);
    
    /**
     * 银行卡支付下单
     */
    String createBankCardPayOrder(PaymentOrder order);
    
    /**
     * 检查并处理过期订单
     */
    void checkAndHandleExpiredOrders();
}