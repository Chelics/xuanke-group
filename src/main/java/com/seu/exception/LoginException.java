package com.seu.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginException extends RuntimeException{
    private HttpStatus status;
    public LoginException(String msg, HttpStatus status){
        super("登录失败: " + msg);
        this.status = status;
    }
}
