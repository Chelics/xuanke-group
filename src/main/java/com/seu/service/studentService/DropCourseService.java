package com.seu.service.studentService;

import com.seu.exception.InvalidInputException;

public interface DropCourseService {
    /**
     * 退课操作
     * @param courseId
     * @param studentId
     * @return
     */
    boolean dropCourse(Integer courseId, Integer studentId) throws InvalidInputException;
}
