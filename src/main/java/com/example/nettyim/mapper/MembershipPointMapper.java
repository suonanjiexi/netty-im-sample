package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.MembershipPoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员积分记录数据访问层
 */
@Mapper
public interface MembershipPointMapper extends BaseMapper<MembershipPoint> {
}