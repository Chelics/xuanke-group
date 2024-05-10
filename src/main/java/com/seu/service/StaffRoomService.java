package com.seu.service;

import com.seu.pojo.PageBean;

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
}
