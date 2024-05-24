package com.seu.utils;

import com.seu.config.JwtConfig;
import com.seu.exception.UserNotLoggedInException;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

/**
 * JwtUtils类
 * 用于生成和校验 JWT 令牌
 */
public class JwtUtils {

    /**
     * 生成 JWT 令牌
     * @param claims
     * @return
     */
    public static String generateJwt(Map<String, Object> claims, JwtConfig jwtConfig){

        String signKey = jwtConfig.getSignKey();
        Long expire = jwtConfig.getExpire();

        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    /**
     * 解析 JWT令牌
     * @param jwt
     * @return
     */
    public static Claims parseJWT(String jwt, JwtConfig jwtConfig) throws UserNotLoggedInException {

        String signKey = jwtConfig.getSignKey();

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // "Bearer "有7个字符
        }

        try {
            return Jwts.parser()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new UserNotLoggedInException("登录已过期");
        } catch (SignatureException e) {
            throw new UserNotLoggedInException("签名验证失败");
        } catch (Exception e) {
            throw new UserNotLoggedInException("登录校验失败");
        }
    }
}
