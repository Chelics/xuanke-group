package com.seu.service;

import com.seu.mapper.ApplyingMapper;
import com.seu.pojo.ApplyingCourse;
import com.seu.pojo.PageBean;
import com.seu.pojo.Stage;
import java.util.List;

public interface TeacherCourseService {

    /**
     * 申请新增课程

     * @return
     */
    void applyCourse(ApplyingCourse applyingCourse);


    /**
     * 分页查询待审核课程列表
     * @param page
     * @param pageSize
     * @return
     */
    PageBean pageNotReviewed(Integer page, Integer pageSize);

    /**
     * 分页查询已通过课程列表
     * @param page
     * @param pageSize
     * @return
     */
    PageBean pagePassed(Integer page, Integer pageSize);

    /**
     * 分页查询已驳回课程列表
     * @param page
     * @param pageSize
     * @return
     */
    PageBean pageRejected(Integer page, Integer pageSize);

}
