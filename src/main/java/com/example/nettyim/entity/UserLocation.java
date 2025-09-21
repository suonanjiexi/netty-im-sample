package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户位置信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_locations")
public class UserLocation extends BaseEntity {
    
    /**
     * 位置ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 纬度
     */
    private BigDecimal latitude;
    
    /**
     * 经度
     */
    private BigDecimal longitude;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 是否当前位置
     */
    private Boolean isCurrent;
    
    /**
     * 隐私级别：0-不显示，1-好友可见，2-公开
     */
    private Integer privacyLevel;
    
    /**
     * 计算与另一个位置的距离（单位：千米）
     */
    public double calculateDistance(UserLocation other) {
        if (other == null || latitude == null || longitude == null || 
            other.latitude == null || other.longitude == null) {
            return Double.MAX_VALUE;
        }
        
        double lat1 = Math.toRadians(latitude.doubleValue());
        double lon1 = Math.toRadians(longitude.doubleValue());
        double lat2 = Math.toRadians(other.latitude.doubleValue());
        double lon2 = Math.toRadians(other.longitude.doubleValue());
        
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        
        double a = Math.sin(dlat/2) * Math.sin(dlat/2) + 
                  Math.cos(lat1) * Math.cos(lat2) * 
                  Math.sin(dlon/2) * Math.sin(dlon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        
        return 6371 * c; // 地球半径约6371公里
    }
}