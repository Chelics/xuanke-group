package com.seu.service.studentService;

import com.seu.pojo.FullCourse;

import java.util.List;

public interface SearchCourseService {
    List<FullCourse> searchCoursesByKeyWord(String keyWord);
}
