package com.seu.service;

import com.seu.pojo.Course;
import com.seu.pojo.PageBean;
import com.seu.pojo.RoomCourse;

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
    List<RoomCourse> getCoursesByRoomId(Integer id);
}
