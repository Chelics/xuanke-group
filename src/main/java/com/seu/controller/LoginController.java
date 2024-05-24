package com.seu.controller;

import com.seu.config.JwtConfig;
import com.seu.dto.request.LoginData;
import com.seu.exception.InvalidInputException;
import com.seu.exception.LoginException;
import com.seu.pojo.Result;
import com.seu.pojo.Users.User;
import com.seu.service.LoginService;
import com.seu.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    JwtConfig jwtConfig;
    /**
     * 登录验证
     * @param request
     * @param loginData
     * @return
     */
    @PostMapping
    public Result login(@RequestBody(required = false) LoginData loginData, HttpServletRequest request) throws InvalidInputException {
        log.info("登录尝试来自IP: " + request.getRemoteAddr());

        if(loginData == null) {
            log.warn("用户登录时请求体为空: " + request.getRemoteAddr());
            throw new InvalidInputException("用户名或密码不能为空");
        }

        String username = loginData.getUsername();
        String password = loginData.getPassword();

        //输入有效性检查
        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()){
            log.warn("用户名或密码为空: " + request.getRemoteAddr());
            throw new InvalidInputException("用户名或密码不能为空");
        }
        if(username.length() != 9 || password.length() < 6 || password.length() > 20){
            throw new InvalidInputException("用户名或密码错误");
        }

        //转到业务层验证
        User user = loginService.checkCredentials(username, password);
        if(user != null){
            //下发 JWT 令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            claims.put("name", user.getName());

            String jwt = JwtUtils.generateJwt(claims, jwtConfig);  //缺陷: 此处如果user为学生, payload中不包含classId
            log.info("用户登录成功: " + user.getId());
            return Result.success(jwt);
        }

        else {
            log.warn("用户名或密码错误");
            throw new LoginException("用户名或密码错误", HttpStatus.UNAUTHORIZED);
        }
    }
}
