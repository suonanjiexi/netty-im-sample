package com.example.nettyim.mapper;

import com.example.nettyim.entity.ForumReplyLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 贴吧回复点赞Mapper接口
 */
@Mapper
public interface ForumReplyLikeMapper extends BaseMapper<ForumReplyLike> {
}