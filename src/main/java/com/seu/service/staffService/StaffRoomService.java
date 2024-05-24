package com.seu.service.staffService;

import com.seu.pojo.PageBean;
import com.seu.pojo.FullCourse;

import java.util.List;

public interface StaffRoomService {
    /**
     * 分页查询教室列表
     * @param page
     * @param pageSize
     * @param building
     * @param roomName
     * @param storageBegin
     * @param storageEnd
     * @return
     */
    PageBean page(Integer page, Integer pageSize, String building, String roomName, Short storageBegin, Short storageEnd);

    /**
     * 根据教室ID查询课表
     * @param id
     * @return
     */
    List<FullCourse> getCoursesByRoomId(Integer id);
}
