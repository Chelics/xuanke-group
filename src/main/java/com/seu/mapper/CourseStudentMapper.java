package com.seu.mapper;


import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface CourseStudentMapper {


    /**
     * 添加课程到学生课表
     * @param courseId
     * @param studentId
     */
    @Transactional(rollbackFor = Exception.class)
    @Insert("INSERT INTO course_student(course_id, student_id) VALUES(#{courseId}, #{studentId})")
    int selectCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    /**
     * 计数选择某一门课程的学生人数
     * @param courseId
     * @return
     */
    @Select("SELECT course_id FROM course_student WHERE course_id=#{courseId}")
    Integer getStudentCountForCourse(Integer courseId);

    /**
     * 根据学生id获取课程列表
     * @param studentId
     * @return
     */
    @Select("SELECT course_id FROM course_student WHERE student_id=#{studentId}")
    List<Integer> getCourseIdsByStudentId(Integer studentId);

    /**
     * 根据学生id和课程id删除课程(退课)
     * @param id
     * @param studentId
     * @return
     */
    @Delete("DELETE FROM course_student WHERE course_id=#{courseId} AND student_id=#{studentId}")
    Integer deleteByStudentIdAndCourseId(@Param("courseId")Integer id, @Param("studentId")Integer studentId);
}
