package com.seu.service.teacherService;

import com.seu.pojo.FullCourse;

import java.util.List;

public interface TeacherCourseTableService {
    /**
     * 根据教师ID查询其课表
     * @param teacherId
     * @return
     */
    List<FullCourse> getCoursesByTeacherId(Integer teacherId);
}
