package com.seu.service.studentService;

import com.seu.exception.EntityNotFoundException;
import com.seu.pojo.FullCourse;

import java.util.List;

public interface GetAllCoursesService {
    /**
     * 获取所有课程
     * @return
     */
    List<FullCourse> getAllCourse(Integer studentId) throws EntityNotFoundException;
}
