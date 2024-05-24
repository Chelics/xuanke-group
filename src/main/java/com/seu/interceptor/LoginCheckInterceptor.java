package com.seu.interceptor;

import com.seu.config.JwtConfig;
import com.seu.exception.UserNotLoggedInException;
import com.seu.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 拦截器类, 用于登录校验
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    JwtConfig jwtConfig;
    @Override
    public boolean preHandle(HttpServletRequest req, @NotNull HttpServletResponse resp, Object handler) throws Exception {

        String url = req.getRequestURI();
        log.info("已拦截请求的url: {}", url);

        //登录操作, 直接放行
        //此操作冗余, 因为LoginCheckInterceptor类已设置不会拦截login
        if(url.contains("login")){
            return true;
        }

        String jwt = req.getHeader("Authorization");

        //没有令牌, 不放行
        if(!StringUtils.hasLength(jwt)){
            throw new UserNotLoggedInException("用户未登录");
        }

        //解析(如果失败, 不放行)
        Claims claims = JwtUtils.parseJWT(jwt, jwtConfig);

        //存储claims为请求属性
        req.setAttribute("claims", claims);

        //放行
        log.info("放行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
