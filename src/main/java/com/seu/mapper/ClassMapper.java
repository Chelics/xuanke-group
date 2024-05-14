package com.seu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
