package com.example.nettyim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Netty IM 应用启动类
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, RedisAutoConfiguration.class})
@MapperScan("com.example.nettyim.mapper")
public class NettyImApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyImApplication.class, args);
    }
}