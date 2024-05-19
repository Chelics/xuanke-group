package com.seu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据教师id列表获取课程id列表
     * @param teacherIds
     * @return
     */
    List<Integer> getCoursesByTeachers(@Param("teacherIds") List<Integer> teacherIds);

}
