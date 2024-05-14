//package com.seu.service;
//
//import com.seu.config.SecurityConfig;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.junit.Test;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 单元测试类
// * 仅作单元测试使用, 包含各种毫不相关的测试, 移除后不影响程序运行
// */
//@Service
//public class UnitTest {
//    /**
//     * 测试JWT令牌生成
//     */
//    @Test
//    public void testGenJwt(){
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("id", 10086);
//        claims.put("name", "张三");
//
//        String jwt = Jwts.builder()
//                .signWith(SignatureAlgorithm.HS256, "SEUxuanke")//签名算法
//                .setClaims(claims)//自定义内容(载荷)
//                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))//设置有效期为1h
//                .compact();
//        System.out.println(jwt);
//    }
//
//    /**
//     * 测试JWT令牌解析
//     */
//    @Test
//    public void testParseJwt(){
//        Claims claims = Jwts.parser()
//                .setSigningKey("SEUxuanke")
//                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5byg5LiJIiwiaWQiOjEwMDg2LCJleHAiOjE3MTU1MTQxMTN9.ZsZA7TFYWV3FlenqKAs0GxKrAaLJBLk5gJEEFUPoCpU")
//                .getBody();
//        System.out.println(claims);
//    }
//
//    /**
//     * 测试密码加密
//     */
//    @Test
//    public void testEncodePassword(){
//        SecurityConfig securityConfig = new SecurityConfig();
//        String encodedPassword = securityConfig.encodePassword("123456");
//        System.out.println(encodedPassword);
//
//        boolean mark = securityConfig.matchPassword("123456", encodedPassword);
//        System.out.println(mark);
//    }
//
//    /**
//     * 测试密码校验
//     */
//    @Test
//    public void testMatchPassword(){
//
//    }
//}
