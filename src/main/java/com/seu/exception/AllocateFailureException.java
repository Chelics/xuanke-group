package com.seu.exception;

import lombok.Getter;

import java.util.List;

public class AllocateFailureException extends Exception{
    @Getter
    private List<Integer> failedCourses;
    public AllocateFailureException(String msg){
        super("分配课程失败: " + msg);
    }
}
