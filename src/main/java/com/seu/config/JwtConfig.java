package com.seu.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 * 用于从application.yml里读取JWT密钥和有效期
 */
@Getter
@Configuration
public class JwtConfig {
    @Value("${jwt.signKey}")
    private String signKey;

    @Value("${jwt.expire}")
    private Long expire;

}


