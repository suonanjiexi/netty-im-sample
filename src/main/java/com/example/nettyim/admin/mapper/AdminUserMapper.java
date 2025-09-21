package com.example.nettyim.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nettyim.admin.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 管理员用户Mapper接口
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
    
    /**
     * 根据用户名查找管理员
     * @param username 用户名
     * @return 管理员用户
     */
    @Select("SELECT * FROM admin_users WHERE username = #{username} AND status = 1")
    AdminUser findByUsername(@Param("username") String username);
}