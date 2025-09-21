package com.example.nettyim.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nettyim.entity.MembershipLevel;
import com.example.nettyim.entity.UserMembership;
import com.example.nettyim.service.MembershipService;

/**
 * 会员管理服务接口（扩展）
 */
public interface MembershipManagementService extends MembershipService {
    
    /**
     * 分页查询会员等级列表
     * @param page 分页参数
     * @return 会员等级分页列表
     */
    IPage<MembershipLevel> pageMembershipLevels(Page<MembershipLevel> page);
    
    /**
     * 根据ID获取会员等级
     * @param id 会员等级ID
     * @return 会员等级信息
     */
    MembershipLevel getMembershipLevelById(Long id);
    
    /**
     * 创建会员等级
     * @param membershipLevel 会员等级信息
     * @return 是否成功
     */
    boolean createMembershipLevel(MembershipLevel membershipLevel);
    
    /**
     * 更新会员等级
     * @param id 会员等级ID
     * @param membershipLevel 会员等级信息
     * @return 是否成功
     */
    boolean updateMembershipLevel(Long id, MembershipLevel membershipLevel);
    
    /**
     * 删除会员等级
     * @param id 会员等级ID
     * @return 是否成功
     */
    boolean deleteMembershipLevel(Long id);
    
    /**
     * 分页查询用户会员列表
     * @param page 分页参数
     * @param userId 用户ID
     * @param levelId 会员等级ID
     * @param status 会员状态
     * @return 用户会员分页列表
     */
    IPage<UserMembership> pageUserMemberships(Page<UserMembership> page, Long userId, Long levelId, Integer status);
    
    /**
     * 根据ID获取用户会员信息
     * @param id 用户会员ID
     * @return 用户会员信息
     */
    UserMembership getUserMembershipById(Long id);
    
    /**
     * 更新用户会员状态
     * @param id 用户会员ID
     * @param status 会员状态
     * @return 是否成功
     */
    boolean updateUserMembershipStatus(Long id, Integer status);
}