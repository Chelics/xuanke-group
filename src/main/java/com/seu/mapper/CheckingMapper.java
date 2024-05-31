package com.seu.mapper;

import com.seu.pojo.CheckingCourse;
import com.seu.pojo.FullCheckingCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CheckingMapper {
    /**
     * 分页获取待审核课程信息
     * 注意状态为1才是待审核
     * @param courseName
     * @return
     */
    List<FullCheckingCourse> list(String courseName);

    /**
     * 驳回请求
     * @param ids
     */
    void reject(@Param("ids")List<Integer> ids);

    /**
     * 通过请求
     * @param ids
     */
    void pass(@Param("ids")List<Integer> ids);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Select("select * from checking where id=#{id}")
    FullCheckingCourse getById(Integer id);

    /**
     * 新增上报课程
     * @param applyingCourse
     */
    @Update("insert into checking (course_number, course_name, type, course_hour, course_storage, class_ids, teacher_ids, faculty, credit, commit_time) " +
            "values (#{courseNumber},#{courseName},#{type},#{courseHour},#{courseStorage}, #{classIds}, #{teacherIds}, #{faculty}, #{credit}, #{commitTime})")
    void add(CheckingCourse applyingCourse);

    /**
     * 教师查看各状态申请
     * @param id
     * @param status
     * @return
     */
    List<FullCheckingCourse> page(@Param("id") Integer id,  @Param("status")Short status);

    List<FullCheckingCourse> page2(@Param("id") Integer id);
}
