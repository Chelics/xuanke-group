package com.seu.service;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;

public interface CourseScheduler {
    /**
     * 为课程分配时间和教室
     * @param id
     */
    void allocateTimeAndRoom(Integer id) throws EntityNotFoundException, InvalidInputException;

    /**
     * 为课程分配时间
     * @param id
     */
    void classifyTime(Integer id) throws EntityNotFoundException, InvalidInputException;

    /**
     * 为课程分配教室
     * @param id
     */
    void allocateRoom(Integer id);
}
