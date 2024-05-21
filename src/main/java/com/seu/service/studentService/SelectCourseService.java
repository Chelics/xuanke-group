package com.seu.service.studentService;

import com.seu.exception.SelectCourseFailureException;

public interface SelectCourseService {
    /**
     * 学生选课
     * @param courseId
     * @param studentId
     * @return
     */
    boolean selectCourse(Integer courseId, Integer studentId) throws SelectCourseFailureException;
}
