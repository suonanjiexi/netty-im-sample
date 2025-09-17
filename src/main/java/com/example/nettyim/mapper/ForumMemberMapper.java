package com.example.nettyim.mapper;

import com.example.nettyim.entity.ForumMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 贴吧成员Mapper接口
 */
@Mapper
public interface ForumMemberMapper extends BaseMapper<ForumMember> {
}