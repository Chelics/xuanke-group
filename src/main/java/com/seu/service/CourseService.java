package com.seu.service;

import com.seu.pojo.FullCourse;

import java.util.List;

public interface CourseService {
    /**
     * 根据课程基本信息查询完整信息
     * @param fullCourse
     * @return
     */
    void getFullByBasic(FullCourse fullCourse);

    void getFullsByBasics(List<FullCourse> fullCourses);
}
