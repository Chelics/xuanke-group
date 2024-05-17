package com.seu.exception;

public class EntityNotFoundException extends Exception{
    public EntityNotFoundException(String msg){
        super("未查询到结果: " + msg);
    }
}
