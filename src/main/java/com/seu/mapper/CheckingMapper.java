package com.seu.mapper;

import com.seu.pojo.FullCheckingCourse;
import org.apache.ibatis.annotations.Mapper;

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
}
