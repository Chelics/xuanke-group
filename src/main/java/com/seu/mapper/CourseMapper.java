package com.seu.mapper;

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
     * @param id
     * @return
     */
    List<FullCourse> getCoursesByRoomIds(@Param("roomIds") List<Integer> roomIds);

    /**
     * 检查对应id的课程是否存在
     * @param id
     * @return
     */
    @Select("SELECT COUNT(id) FROM course WHERE id=#{id}")
    int checkCourseExist(Integer id);

    /**
     * 根据关键词搜索课程
     * @param keyWord
     * @return
     */
    List<FullCourse> searchCoursesByKeyWord(@Param("keyWord") String keyWord,
                                            @Param("idsSearchedByTeacher")List<Integer> idsSearchedByTeacher,
                                            @Param("idsSearchedByRoom")List<Integer> idsSearchedByRoom);
}
