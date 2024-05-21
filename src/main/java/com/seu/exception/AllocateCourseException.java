package com.seu.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class AllocateCourseException extends Exception{
    private List<Integer> failedCourses;
    public AllocateCourseException(String msg){
        super("分配课程失败: " + msg);
    }
}
