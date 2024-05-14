package com.seu.service;

import com.seu.pojo.PageBean;
import org.springframework.stereotype.Service;

public interface StaffCheckingService {
    /**
     * 分页查询待审核列表
     * @param page
     * @param pageSize
     * @param courseName
     * @return
     */
    PageBean page(Integer page, Integer pageSize, String courseName);
}
