package com.seu.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SelectCourseRuntimeException extends RuntimeException{
    private HttpStatus status;
    public SelectCourseRuntimeException(String msg, HttpStatus status){
        super(msg);
        this.status = status;
    }
    public SelectCourseRuntimeException(String msg){
        super(msg);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
