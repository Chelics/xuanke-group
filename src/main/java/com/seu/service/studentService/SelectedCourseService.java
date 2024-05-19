package com.seu.service.studentService;

import com.seu.pojo.FullCourse;

import java.util.List;

public interface SelectedCourseService {
    List<FullCourse> getSelectedCourse(Integer studentId);
}
