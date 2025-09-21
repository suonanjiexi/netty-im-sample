package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.MembershipLevel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级数据访问层
 */
@Mapper
public interface MembershipLevelMapper extends BaseMapper<MembershipLevel> {
}