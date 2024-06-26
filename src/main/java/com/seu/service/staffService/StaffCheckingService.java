package com.seu.service.staffService;


import com.seu.exception.AllocateCourseException;
import com.seu.pojo.FullCheckingCourse;
import com.seu.pojo.PageBean;

import java.util.List;

public interface StaffCheckingService {
    /**
     * 分页查询待审核列表
     * @param page
     * @param pageSize
     * @param courseName
     * @return
     */
    PageBean page(Integer page, Integer pageSize, String courseName);

    /**
     * 驳回请求
     * @param ids
     */
    void reject(List<Integer> ids);

    /**
     * 通过请求
     * @param ids
     */
    void pass(List<Integer> ids) throws AllocateCourseException;

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    FullCheckingCourse getById(Integer id);
}
