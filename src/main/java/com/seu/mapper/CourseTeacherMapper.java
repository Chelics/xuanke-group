package com.seu.mapper;

import com.seu.pojo.FullCourse;
import org.apache.ibatis.annotations.Insert;
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

    /**
     * 添加课程信息
     * @param courseId
     * @param teacherId
     * @return
     */
    @Insert("INSERT INTO course_teacher(course_id, teacher_id) VALUES(#{courseId}, #{teacherId}) ON DUPLICATE KEY UPDATE course_id = VALUES(course_id), teacher_id = VALUES(teacher_id)")
    int insertCourseTeacher(@Param("courseId") Integer courseId, @Param("teacherId") Integer teacherId);

    /**
     * 根据教师ID查询授课ID
     * @param teacherId
     * @return
     */
    @Select("select course_id from course_teacher where teacher_id=#{teacherId}")
    List<Integer> getCourseIdsByTeacherId(Integer teacherId);
}
