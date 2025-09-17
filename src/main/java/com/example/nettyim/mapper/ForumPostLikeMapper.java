package com.example.nettyim.mapper;

import com.example.nettyim.entity.ForumPostLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 贴吧帖子点赞Mapper接口
 */
@Mapper
public interface ForumPostLikeMapper extends BaseMapper<ForumPostLike> {
}