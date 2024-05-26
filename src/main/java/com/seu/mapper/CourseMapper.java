package com.seu.mapper;

import com.seu.dto.response.FullFullCourse;
import com.seu.pojo.Course;
import com.seu.pojo.FullCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {

    /**
     * 查询全部课程
     * @return
     */
    @Select("SELECT * FROM course")
    List<FullCourse> list();

    /**
     * 根据id查询课程
     * @param id
     * @return
     */
    @Select("select * from course where id=#{id}")
    FullCourse getCourseById(Integer id);

    /**
     * 根据id列表查询课程列表
     * @param ids
     * @return
     */
    List<FullCourse> getCoursesByIds(@Param("ids") List<Integer> ids);

    /**
     * 查询某教室的课程
     * @param id
     * @return
     */
    @Select("select * from course where room_id=#{id}")
    List<FullCourse> getCoursesByRoomId(Integer id);

    /**
     * 查询多个教室的课程
     * @param roomIds
     * @return
     */
    List<FullCourse> getCoursesByRoomIds(@Param("roomIds") List<Integer> roomIds);

    /**
     * 根据关键词搜索课程
     * @param keyWord
     * @return
     */
    List<FullFullCourse> searchCoursesByKeyWord(@Param("keyWord") String keyWord,
                                            @Param("idsSearchedByTeacher")List<Integer> idsSearchedByTeacher,
                                            @Param("idsSearchedByRoom")List<Integer> idsSearchedByRoom);

    /**
     * 添加课程
     * @param course
     * @return
     */
    int insertCourse(Course course);

    /**
     * 获取通选课
     * @return
     */
    @Select("SELECT * FROM course WHERE type IN (1, 2)")
    List<FullFullCourse> getUniversalCourses();

    /**
     * 根据id列表查询课程列表
     * @param ids
     * @return
     */
    List<FullFullCourse> getCoursesByIdss(@Param("ids") List<Integer> ids);
}
