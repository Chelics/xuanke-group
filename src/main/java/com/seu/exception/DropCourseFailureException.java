package com.seu.exception;

public class DropCourseFailureException extends Exception{
    public DropCourseFailureException(String msg){
        super("退课失败: " + msg);
    }
}
