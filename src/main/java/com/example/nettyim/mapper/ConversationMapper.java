package com.example.nettyim.mapper;

import com.example.nettyim.entity.Conversation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话Mapper接口
 */
@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
}