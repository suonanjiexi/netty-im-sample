package com.example.nettyim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.PaymentMethod;
import com.example.nettyim.entity.PaymentOrder;
import com.example.nettyim.mapper.PaymentMethodMapper;
import com.example.nettyim.mapper.PaymentOrderMapper;
import com.example.nettyim.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付服务实现类
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    
    @Autowired
    private PaymentOrderMapper paymentOrderMapper;
    
    @Override
    public List<PaymentMethod> getAvailablePaymentMethods() {
        LambdaQueryWrapper<PaymentMethod> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentMethod::getIsActive, true)
                .orderByAsc(PaymentMethod::getSortOrder);
        return paymentMethodMapper.selectList(wrapper);
    }
    
    @Override
    public PaymentMethod getPaymentMethodByCode(String methodCode) {
        LambdaQueryWrapper<PaymentMethod> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentMethod::getMethodCode, methodCode)
                .eq(PaymentMethod::getIsActive, true);
        return paymentMethodMapper.selectOne(wrapper);
    }
    
    @Override
    @Transactional
    public PaymentOrder createPaymentOrder(Long userId, Integer productType, Long productId, 
                                          String productName, BigDecimal amount, Long paymentMethodId) {
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(PaymentOrder.generateOrderNo());
        order.setUserId(userId);
        order.setProductType(productType);
        order.setProductId(productId);
        order.setProductName(productName);
        order.setOriginalAmount(amount);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setFinalAmount(amount);
        order.setPaymentMethodId(paymentMethodId);
        order.setPaymentStatus(0); // 待支付
        order.setExpireTime(LocalDateTime.now().plusHours(2)); // 2小时后过期
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        paymentOrderMapper.insert(order);
        return order;
    }
    
    @Override
    public PaymentOrder getPaymentOrder(String orderNo) {
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getOrderNo, orderNo);
        return paymentOrderMapper.selectOne(wrapper);
    }
    
    @Override
    @Transactional
    public Boolean handlePaymentSuccess(String orderNo, String tradeNo, String callbackData) {
        PaymentOrder order = getPaymentOrder(orderNo);
        if (order == null || order.getPaymentStatus() != 0) {
            return false;
        }
        
        order.setPaymentStatus(2); // 支付成功
        order.setTradeNo(tradeNo);
        order.setPaidTime(LocalDateTime.now());
        order.setCallbackData(callbackData);
        order.setUpdatedAt(LocalDateTime.now());
        
        return paymentOrderMapper.updateById(order) > 0;
    }
    
    @Override
    @Transactional
    public Boolean handlePaymentFailure(String orderNo, String reason) {
        PaymentOrder order = getPaymentOrder(orderNo);
        if (order == null) {
            return false;
        }
        
        order.setPaymentStatus(3); // 支付失败
        order.setRemark(reason);
        order.setUpdatedAt(LocalDateTime.now());
        
        return paymentOrderMapper.updateById(order) > 0;
    }
    
    @Override
    @Transactional
    public Boolean cancelPaymentOrder(String orderNo) {
        PaymentOrder order = getPaymentOrder(orderNo);
        if (order == null || !order.canPay()) {
            return false;
        }
        
        order.setPaymentStatus(5); // 已取消
        order.setUpdatedAt(LocalDateTime.now());
        
        return paymentOrderMapper.updateById(order) > 0;
    }
    
    @Override
    @Transactional
    public Boolean requestRefund(String orderNo, BigDecimal refundAmount, String refundReason) {
        PaymentOrder order = getPaymentOrder(orderNo);
        if (order == null || !order.canRefund()) {
            return false;
        }
        
        order.setPaymentStatus(4); // 已退款
        order.setRefundAmount(refundAmount);
        order.setRefundTime(LocalDateTime.now());
        order.setRefundReason(refundReason);
        order.setUpdatedAt(LocalDateTime.now());
        
        return paymentOrderMapper.updateById(order) > 0;
    }
    
    @Override
    @Transactional
    public Boolean handleRefundSuccess(String orderNo, BigDecimal refundAmount) {
        PaymentOrder order = getPaymentOrder(orderNo);
        if (order == null) {
            return false;
        }
        
        order.setRefundAmount(refundAmount);
        order.setRefundTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        return paymentOrderMapper.updateById(order) > 0;
    }
    
    @Override
    public IPage<PaymentOrder> getUserPaymentOrders(Long userId, Page<PaymentOrder> page) {
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getUserId, userId)
                .orderByDesc(PaymentOrder::getCreatedAt);
        return paymentOrderMapper.selectPage(page, wrapper);
    }
    
    @Override
    public String createWeChatPayOrder(PaymentOrder order) {
        // 这里应该调用微信支付API创建订单
        // 返回支付二维码或支付链接
        log.info("创建微信支付订单: {}", order.getOrderNo());
        return "wechat_pay_url_" + order.getOrderNo();
    }
    
    @Override
    public String createAlipayOrder(PaymentOrder order) {
        // 这里应该调用支付宝API创建订单
        // 返回支付链接
        log.info("创建支付宝订单: {}", order.getOrderNo());
        return "alipay_url_" + order.getOrderNo();
    }
    
    @Override
    public String createBankCardPayOrder(PaymentOrder order) {
        // 这里应该调用银行卡支付API创建订单
        log.info("创建银行卡支付订单: {}", order.getOrderNo());
        return "bank_card_url_" + order.getOrderNo();
    }
    
    @Override
    public void checkAndHandleExpiredOrders() {
        LambdaQueryWrapper<PaymentOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentOrder::getPaymentStatus, 0)
                .lt(PaymentOrder::getExpireTime, LocalDateTime.now());
        
        List<PaymentOrder> expiredOrders = paymentOrderMapper.selectList(wrapper);
        
        for (PaymentOrder order : expiredOrders) {
            order.setPaymentStatus(5); // 已取消
            order.setUpdatedAt(LocalDateTime.now());
            paymentOrderMapper.updateById(order);
            log.info("订单已过期并取消: {}", order.getOrderNo());
        }
    }
}