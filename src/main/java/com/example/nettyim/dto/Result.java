package com.example.nettyim.dto;

import lombok.Data;

/**
 * 通用响应结果
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }
    
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }
    
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }
}