package com.example.nettyim.mapper;

import com.example.nettyim.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 群组Mapper接口
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {
}