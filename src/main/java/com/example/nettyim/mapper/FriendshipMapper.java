package com.example.nettyim.mapper;

import com.example.nettyim.entity.Friendship;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好友关系Mapper接口
 */
@Mapper
public interface FriendshipMapper extends BaseMapper<Friendship> {
}