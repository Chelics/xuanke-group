package com.seu.exception;

public class UserNotLoggedInException extends Exception{
    public UserNotLoggedInException(String msg){
        super("登录校验失败: " + msg);
    }
}
