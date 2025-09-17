package com.example.nettyim.mapper;

import com.example.nettyim.entity.Forum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 贴吧Mapper接口
 */
@Mapper
public interface ForumMapper extends BaseMapper<Forum> {
}