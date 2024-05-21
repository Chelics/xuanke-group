package com.seu.controller;

import com.seu.config.JwtConfig;
import com.seu.dto.request.LoginData;
import com.seu.pojo.Result;
import com.seu.pojo.Users.User;
import com.seu.service.LoginService;
import com.seu.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Result> login(@RequestBody LoginData loginData, HttpServletRequest request){
        log.info("登录尝试来自IP: " + request.getRemoteAddr());

        String username = loginData.getUsername();
        String password = loginData.getPassword();

        //输入有效性检查
        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error("用户名和密码不能为空"));

        //转到业务层验证
        User user = loginService.checkCredentials(username, password);
        if(user != null){
            //下发 JWT 令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            claims.put("name", user.getName());

            String jwt = JwtUtils.generateJwt(claims, jwtConfig);  //缺陷: 此处如果user为学生, jwt中不包含classId
            return ResponseEntity.ok(Result.success(jwt));
        }

        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error("用户名或密码错误"));
    }
}
