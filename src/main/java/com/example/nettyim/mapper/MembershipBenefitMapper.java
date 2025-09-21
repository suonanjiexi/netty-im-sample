package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.MembershipBenefit;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员权益数据访问层
 */
@Mapper
public interface MembershipBenefitMapper extends BaseMapper<MembershipBenefit> {
}