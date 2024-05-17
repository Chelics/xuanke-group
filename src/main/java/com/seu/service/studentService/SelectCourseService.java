package com.seu.service.studentService;

import com.seu.exception.SelectCourseFailureException;

public interface SelectCourseService {
    boolean selectCourse(Integer courseId, Integer studentId) throws SelectCourseFailureException;
}
