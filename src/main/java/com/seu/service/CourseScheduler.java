package com.seu.service;

import com.seu.exception.AllocateFailureException;
import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.pojo.CheckingCourse;

import java.util.List;

public interface CourseScheduler {

    /**
     * 为列表中的所有课程分配时间和教室
     * @param courseIds
     * @return  返回分配失败的课程列表
     * @throws AllocateFailureException
     */
    public List<Integer> allocateMultipleCourses(List<Integer> courseIds) throws AllocateFailureException;

    /**
     * 为课程分配时间和教室
     * @param id
     */
    void allocateTimeAndRoom(Integer id) throws EntityNotFoundException, InvalidInputException, AllocateFailureException;

    /**
     * 为课程分配时间
     * @param id
     */
    short[] classifyTime(Integer id, CheckingCourse checkingCourse) throws EntityNotFoundException, InvalidInputException, AllocateFailureException;

    /**
     * 为课程分配教室
     * @param id
     */
    Integer getFreeRoom(Integer id, short[] times, CheckingCourse checkingCourse) throws AllocateFailureException;
}
