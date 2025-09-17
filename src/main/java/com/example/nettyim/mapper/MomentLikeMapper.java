package com.example.nettyim.mapper;

import com.example.nettyim.entity.MomentLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 朋友圈点赞Mapper接口
 */
@Mapper
public interface MomentLikeMapper extends BaseMapper<MomentLike> {
}