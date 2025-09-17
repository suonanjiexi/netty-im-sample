package com.example.nettyim.mapper;

import com.example.nettyim.entity.UserSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户会话Mapper接口
 */
@Mapper
public interface UserSessionMapper extends BaseMapper<UserSession> {
}