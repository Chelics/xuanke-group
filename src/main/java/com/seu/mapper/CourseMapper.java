package com.seu.mapper;

import com.seu.pojo.FullCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * 查询某教室的课程
     * @param id
     * @return
     */
    @Select("select * from course where room_id=#{id}")
    List<FullCourse> getCoursesByRoomId(Integer id);

}
