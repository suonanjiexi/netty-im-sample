package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.PaymentMethod;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付方式数据访问层
 */
@Mapper
public interface PaymentMethodMapper extends BaseMapper<PaymentMethod> {
}