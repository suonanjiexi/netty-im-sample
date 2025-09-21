package com.example.nettyim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户等级实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_levels")
public class UserLevel extends BaseEntity {
    
    /**
     * 等级ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 等级
     */
    private Integer level;
    
    /**
     * 等级名称
     */
    private String levelName;
    
    /**
     * 最小经验值
     */
    private Integer minExp;
    
    /**
     * 最大经验值
     */
    private Integer maxExp;
    
    /**
     * 等级图标
     */
    private String iconUrl;
    
    /**
     * 等级特权（JSON格式）
     */
    private String privileges;
    
    /**
     * 检查经验值是否在当前等级范围内
     */
    public boolean isExpInRange(Integer exp) {
        if (exp == null) {
            return false;
        }
        
        if (exp < minExp) {
            return false;
        }
        
        return maxExp == null || exp <= maxExp;
    }
}