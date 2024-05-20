package com.seu.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SelectCourseException extends Exception{
    private HttpStatus status;
    public SelectCourseException(String msg, HttpStatus status){
        super("选课失败: " + msg);
        this.status = status;
    }
}
