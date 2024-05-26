package com.seu.service.studentService;

import com.seu.dto.response.FullFullCourse;

import java.util.List;

public interface SearchCourseService {
    /**
     * 通过关键词搜索课程, 支持的字段: 课程名, 课程编号, 开设学院, 教师姓名, 教室
     * @param keyWord
     * @return
     */
    List<FullFullCourse> searchCoursesByKeyWord(String keyWord);
}
