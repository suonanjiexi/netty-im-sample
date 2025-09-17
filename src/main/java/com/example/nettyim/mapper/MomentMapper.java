package com.example.nettyim.mapper;

import com.example.nettyim.entity.Moment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 朋友圈动态Mapper接口
 */
@Mapper
public interface MomentMapper extends BaseMapper<Moment> {
}