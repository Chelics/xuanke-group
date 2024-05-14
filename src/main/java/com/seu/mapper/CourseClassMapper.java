package com.seu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
