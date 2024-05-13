package com.seu.mapper;


import com.seu.pojo.Users.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * 教务人员Mapper
 */

@Mapper
public interface StaffMapper {

    /**
     * 根据 username查询教务人员
     * @param username
     * @return
     */
    @Select("select * from staff where username=#{username}")
    @Results({
            @Result(property = "password", column = "staff_password"),
            @Result(property = "name", column = "staff_name"),
    })
    Staff getByUsername(String username);
}
