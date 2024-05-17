package com.seu.exception;

public class SelectCourseFailureException extends Exception{
    public SelectCourseFailureException(String msg){
        super("选课失败: " + msg);
    }
}
