package com.seu.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.seu.pojo.Result;
import com.seu.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器类, 用于登录校验
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        String url = req.getRequestURI().toString();
        log.info("已拦截请求的url: {}", url);

        //登录操作, 直接放行
        //此操作冗余, 因为LoginCheckInterceptor类已设置不会拦截login
        if(url.contains("login")){
            return true;
        }

        String jwt = req.getHeader("token");

        //没有令牌, 不放行
        if(!StringUtils.hasLength(jwt)){
            Result error = Result.error("Not log in");
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }

        //解析失败, 不放行
        try{
            JwtUtils.parseJWT(jwt);
        }catch (Exception e){
            e.printStackTrace();
            log.info("解析令牌失败!");
            Result error = Result.error("Not log in");
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
        }

        //放行
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
