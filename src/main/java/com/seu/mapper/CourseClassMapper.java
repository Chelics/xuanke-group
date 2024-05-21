package com.seu.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseClassMapper {
    /**
     * 根据课程ID查询班级ID
     * @param courseId
     * @return
     */
    @Select("select * from course_class where course_id=#{courseId}")
    List<Integer> getClassIdsByCourseId(int courseId);

    /**
     * 根据班级ids查询课程ids
     * @param classIds
     * @return
     */
    List<Integer> getCourseIdsByClassIds(@Param("classIds")List<Integer> classIds);

    /**
     * 添加课程信息
     * @param courseId
     * @param classId
     * @return
     */
    @Insert("INSERT INTO course_class(course_id, class_id) VALUES(#{courseId}, #{classId}) ON DUPLICATE KEY UPDATE course_id = VALUES(course_id), class_id = VALUES(class_id)")
    int insertCourseClass(@Param("courseId")Integer courseId, @Param("classId")Integer classId);

    /**
     * 根据班级id获取课程id列表
     * @param classId
     * @return
     */
    @Select("SELECT course_id FROM course_class WHERE class_id=#{classId}")
    List<Integer> getCoursesByClassId(@Param("classId")Integer classId);
}
