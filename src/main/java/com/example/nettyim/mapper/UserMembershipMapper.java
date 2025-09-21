package com.example.nettyim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.entity.UserMembership;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户会员信息数据访问层
 */
@Mapper
public interface UserMembershipMapper extends BaseMapper<UserMembership> {
}