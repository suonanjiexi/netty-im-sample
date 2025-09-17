package com.example.nettyim.mapper;

import com.example.nettyim.entity.GroupMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 群组成员Mapper接口
 */
@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
}