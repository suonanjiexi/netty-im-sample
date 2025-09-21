package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户会员信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_memberships")
public class UserMembership extends BaseEntity {
    
    /**
     * 会员信息ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 会员等级ID
     */
    private Long levelId;
    
    /**
     * 会员状态：0-停用，1-正常，2-过期，3-暂停
     */
    private Integer status;
    
    /**
     * 会员开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 会员结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 是否试用：0-非试用，1-试用
     */
    private Boolean isTrial;
    
    /**
     * 试用结束时间
     */
    private LocalDateTime trialEndTime;
    
    /**
     * 是否自动续费：0-不自动，1-自动
     */
    private Boolean autoRenew;
    
    /**
     * 续费类型：1-月费，2-年费
     */
    private Integer renewType;
    
    /**
     * 累计支付金额
     */
    private BigDecimal totalPaid;
    
    /**
     * 累计获得积分
     */
    private Integer pointsEarned;
    
    /**
     * 累计使用积分
     */
    private Integer pointsUsed;
    
    /**
     * 推荐码
     */
    private String referralCode;
    
    /**
     * 推荐人用户ID
     */
    private Long referrerUserId;
    
    /**
     * 获取当前积分余额
     */
    public Integer getCurrentPoints() {
        return (pointsEarned != null ? pointsEarned : 0) - (pointsUsed != null ? pointsUsed : 0);
    }
    
    /**
     * 检查会员是否有效
     */
    public Boolean isValid() {
        if (status == null || status != 1) {
            return false;
        }
        
        if (endTime != null && endTime.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查试用是否过期
     */
    public Boolean isTrialExpired() {
        if (!Boolean.TRUE.equals(isTrial) || trialEndTime == null) {
            return false;
        }
        
        return trialEndTime.isBefore(LocalDateTime.now());
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(Boolean isTrial) {
        this.isTrial = isTrial;
    }

    public LocalDateTime getTrialEndTime() {
        return trialEndTime;
    }

    public void setTrialEndTime(LocalDateTime trialEndTime) {
        this.trialEndTime = trialEndTime;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Integer getRenewType() {
        return renewType;
    }

    public void setRenewType(Integer renewType) {
        this.renewType = renewType;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public Integer getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(Integer pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Long getReferrerUserId() {
        return referrerUserId;
    }

    public void setReferrerUserId(Long referrerUserId) {
        this.referrerUserId = referrerUserId;
    }
}