package com.example.nettyim.mapper;

import com.example.nettyim.entity.MomentComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 朋友圈评论Mapper接口
 */
@Mapper
public interface MomentCommentMapper extends BaseMapper<MomentComment> {
}