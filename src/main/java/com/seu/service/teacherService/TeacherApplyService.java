package com.seu.service.teacherService;

import com.seu.dto.response.ClassList;
import com.seu.dto.response.TeacherList;
import com.seu.pojo.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TeacherApplyService {

    /**
     * 申请新增课程
     * @return
     */
    void applyCourse(CheckingCourse applyingCourse);


    /**
     * 分页查询待审核/已通过/已驳回课程列表
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageBean page(Integer id,Integer page, Integer pageSize, Short status);

    /**
     * 根据教师名模糊查询
     * @return
     */
    TeacherList getByteacherName(String teacherName);

    /**
     * 根据班级名模糊查询
     * @return
     */
    ClassList getByclassName(String className);
}
