package com.seu.service.studentService;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.exception.SelectCourseException;

public interface DropCourseService {
    /**
     * 退课操作
     * @param courseId
     * @param studentId
     * @return
     */
    boolean dropCourse(Integer courseId, Integer studentId) throws InvalidInputException, EntityNotFoundException, SelectCourseException;
}
