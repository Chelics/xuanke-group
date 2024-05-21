package com.seu.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class AllocateFailureException extends Exception{
    private List<Integer> failedCourses;
    public AllocateFailureException(String msg){
        super("分配课程失败: " + msg);
    }
}
