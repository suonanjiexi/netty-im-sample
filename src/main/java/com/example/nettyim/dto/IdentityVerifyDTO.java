package com.example.nettyim.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 身份证实名认证DTO
 */
@Data
public class IdentityVerifyDTO {
    
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    @NotBlank(message = "身份证号码不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", 
             message = "身份证号码格式不正确")
    private String idCardNumber;
    
    @NotBlank(message = "身份证正面照片不能为空")
    private String idCardFrontUrl;
    
    @NotBlank(message = "身份证反面照片不能为空")
    private String idCardBackUrl;
    
    // Getter and Setter methods
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getIdCardNumber() {
        return idCardNumber;
    }
    
    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }
    
    public String getIdCardFrontUrl() {
        return idCardFrontUrl;
    }
    
    public void setIdCardFrontUrl(String idCardFrontUrl) {
        this.idCardFrontUrl = idCardFrontUrl;
    }
    
    public String getIdCardBackUrl() {
        return idCardBackUrl;
    }
    
    public void setIdCardBackUrl(String idCardBackUrl) {
        this.idCardBackUrl = idCardBackUrl;
    }
}