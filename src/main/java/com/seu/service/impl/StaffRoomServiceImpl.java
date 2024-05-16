package com.seu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seu.mapper.*;
import com.seu.pojo.*;
import com.seu.service.CourseService;
import com.seu.service.StaffRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffRoomServiceImpl implements StaffRoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseService courseService;
    @Override
    public PageBean page(Integer page, Integer pageSize, String building, String roomName, Short storageBegin, Short storageEnd) {
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询
        List<Room> roomList= roomMapper.list(building, roomName, storageBegin, storageEnd);
        Page<Room> p=(Page<Room>)roomList;
        //3. 封装pageBean 对象
        PageBean pageBean=new PageBean(p.getResult(),p.getTotal());

        return pageBean;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public List<FullCourse> getCoursesByRoomId(Integer id) {
        List<FullCourse> fullCours = courseMapper.getCoursesByRoomId(id);//仍是基本信息
        for (int i = 0; i < fullCours.size(); i++) {
            FullCourse fullCourse = fullCours.get(i);//对于每一个course
            //补充完整信息
            courseService.getFullByBasic(fullCourse);
        }
        return fullCours;
    }
}
