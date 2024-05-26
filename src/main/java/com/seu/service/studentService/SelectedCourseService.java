package com.seu.service.studentService;

import com.seu.pojo.FullCourse;

import java.util.List;

public interface SelectedCourseService {
    /**
     * 获取已选课程
     * @param studentId
     * @return
     */
    List<FullCourse> getSelectedCourse(Integer studentId);
}
