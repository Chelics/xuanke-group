package com.seu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseTeacherMapper {
    /**
     * 根据课程ID获取老师id
     * @param courseId
     * @return
     */
    @Select("select teacher_id from course_teacher where course_id=#{courseId}")
    List<Integer> getTeacherIdsByCourseId(int courseId);
}
