package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.PaymentOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付订单数据访问层
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {
}