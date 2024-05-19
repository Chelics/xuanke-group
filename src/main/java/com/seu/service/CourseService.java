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


    /**
     * 为列表中的所有课程补全完整信息
     * @param fullCourses
     */
    void getFullsByBasics(List<FullCourse> fullCourses);
}
