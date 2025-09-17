package com.example.nettyim.mapper;

import com.example.nettyim.entity.ForumReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 贴吧回复Mapper接口
 */
@Mapper
public interface ForumReplyMapper extends BaseMapper<ForumReply> {
}