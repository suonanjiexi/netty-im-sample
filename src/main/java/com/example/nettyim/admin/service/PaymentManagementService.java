package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.PaymentMethod;
import com.example.nettyim.entity.PaymentOrder;
import com.example.nettyim.service.PaymentService;

/**
 * 支付管理服务接口（扩展）
 */
public interface PaymentManagementService extends PaymentService {
    
    /**
     * 分页查询支付订单列表
     * @param page 分页参数
     * @param orderNo 订单号
     * @param userId 用户ID
     * @param paymentStatus 支付状态
     * @return 支付订单分页列表
     */
    IPage<PaymentOrder> pagePaymentOrders(Page<PaymentOrder> page, String orderNo, Long userId, Integer paymentStatus);
    
    /**
     * 根据ID获取支付订单
     * @param id 支付订单ID
     * @return 支付订单信息
     */
    PaymentOrder getPaymentOrderById(Long id);
    
    /**
     * 处理退款申请
     * @param id 支付订单ID
     * @param refundReason 退款原因
     * @return 是否成功
     */
    boolean processRefund(Long id, String refundReason);
    
    /**
     * 分页查询支付方式列表
     * @param page 分页参数
     * @return 支付方式分页列表
     */
    IPage<PaymentMethod> pagePaymentMethods(Page<PaymentMethod> page);
    
    /**
     * 根据ID获取支付方式
     * @param id 支付方式ID
     * @return 支付方式信息
     */
    PaymentMethod getPaymentMethodById(Long id);
    
    /**
     * 创建支付方式
     * @param paymentMethod 支付方式信息
     * @return 是否成功
     */
    boolean createPaymentMethod(PaymentMethod paymentMethod);
    
    /**
     * 更新支付方式
     * @param id 支付方式ID
     * @param paymentMethod 支付方式信息
     * @return 是否成功
     */
    boolean updatePaymentMethod(Long id, PaymentMethod paymentMethod);
    
    /**
     * 删除支付方式
     * @param id 支付方式ID
     * @return 是否成功
     */
    boolean deletePaymentMethod(Long id);
    
    /**
     * 更新支付方式状态
     * @param id 支付方式ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updatePaymentMethodStatus(Long id, Integer status);
}