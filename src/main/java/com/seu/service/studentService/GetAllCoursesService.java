package com.seu.service.studentService;

import com.seu.dto.response.FullFullCourse;
import com.seu.exception.EntityNotFoundException;

import java.util.List;

public interface GetAllCoursesService {
    /**
     * 获取所有课程
     * @return
     */
    List<FullFullCourse> getAllCourse(Integer studentId) throws EntityNotFoundException;
}
