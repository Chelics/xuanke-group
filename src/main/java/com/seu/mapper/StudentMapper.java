package com.seu.mapper;

import com.seu.pojo.Users.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentMapper {

    /**
     * 根据username查询学生
     * @param username
     * @return
     */
    @Select("select * from student where username=#{username}")
    @Results({
            @Result(property = "password", column = "student_password"),
            @Result(property = "name", column = "student_name"),
            @Result(property = "classId", column = "class_id")
    })
    Student getStudentByUsername(String username);

    /**
     * 根据id查询学生
     * @param id
     * @return
     */
    @Select("SELECT * FROM student WHERE id=#{id}")
    @Results({
            @Result(property = "name", column = "student_name"),
            @Result(property = "password", column = "student_password"),
            @Result(property = "classId", column = "class_id")
    })
    Student getStudentById(Integer id);

}
