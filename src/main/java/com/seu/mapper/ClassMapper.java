package com.seu.mapper;

import com.seu.dto.response.ClassList;
import com.seu.pojo.MyClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.lang.String;
@Mapper
public interface ClassMapper {
    /**
     * 根据ID批量查询名字
     * @param ids
     * @return
     */
    List<String> getNamesByIds(@Param("ids")List<Integer> ids);

    /**
     * 根据班级名模糊查询
     * @param className
     * @return
     */
    //@Select("select id,class_name from class where class_name like concat('%',#{name},'%')")
    List<MyClass> getByName(@Param("className") String className);
}
