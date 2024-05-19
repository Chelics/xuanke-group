package com.seu.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfig {
    @Value("${jwt.signKey}")
    private String signKey;

    @Value("${jwt.expire}")
    private Long expire;

}


