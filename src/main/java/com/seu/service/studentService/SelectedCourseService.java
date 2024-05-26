package com.seu.service.studentService;

import com.seu.dto.response.FullFullCourse;

import java.util.List;

public interface SelectedCourseService {
    /**
     * 获取已选课程
     * @param studentId
     * @return
     */
    List<FullFullCourse> getSelectedCourse(Integer studentId);
}
