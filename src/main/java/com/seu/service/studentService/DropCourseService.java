package com.seu.service.studentService;

import com.seu.exception.DropCourseFailureException;

public interface DropCourseService {
    /**
     * 退课操作
     * @param courseId
     * @param studentId
     * @return
     */
    boolean dropCourse(Integer courseId, Integer studentId) throws DropCourseFailureException;
}
