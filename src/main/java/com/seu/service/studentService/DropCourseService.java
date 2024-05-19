package com.seu.service.studentService;

import com.seu.exception.DropCourseFailureException;

public interface DropCourseService {
    boolean dropCourse(Integer courseId, Integer studentId) throws DropCourseFailureException;
}
