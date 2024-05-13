package com.seu.mapper;

import com.seu.pojo.Users.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper {

    /**
     * 根据username查询老师密码
     * @param username
     * @return
     */
    @Select("select * from teacher where username=#{username}")
    @Results({
            @Result(property = "password", column = "teacher_password"),
            @Result(property = "name", column = "teacher_name")
    })
    Teacher getByUsername(String username);
}
