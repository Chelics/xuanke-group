package com.seu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@Configuration
public class SecurityConfig {

    /**
     *  使用BCryptPasswordEncoder保证密码存储和校验的安全性
     *  数据库中存储的是加密后的密码
     */
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 加密明文密码
     * @param rawPassword
     * @return
     */
    public String encodePassword(String rawPassword){
        BCryptPasswordEncoder encoder = passwordEncoder();
        return encoder.encode(rawPassword);
    }

    /**
     * 校验明文和加密后的密码
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    public boolean matchPassword(String rawPassword, String encodedPassword){
        BCryptPasswordEncoder encoder = passwordEncoder();
        return encoder.matches(rawPassword, encodedPassword);
    }
}
