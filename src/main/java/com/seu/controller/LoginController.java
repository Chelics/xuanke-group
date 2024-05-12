package com.seu.controller;

import com.seu.pojo.LoginData;
import com.seu.pojo.Result;
import com.seu.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    //构造函数注入保持final
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

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

        if(loginService.checkCredentials(username, password))
            return ResponseEntity.ok(Result.success(username)); //username有待商榷
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error("用户名或密码错误"));
    }
}
