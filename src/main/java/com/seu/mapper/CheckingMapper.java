package com.seu.mapper;

import com.seu.pojo.FullCheckingCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
