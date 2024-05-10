package com.seu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seu.mapper.StaffRoomMapper;
import com.seu.pojo.PageBean;
import com.seu.pojo.Room;
import com.seu.service.StaffRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffRoomServiceImpl implements StaffRoomService {
    @Autowired
    private StaffRoomMapper staffRoomMapper;
    @Override
    public PageBean page(Integer page, Integer pageSize, String building, String roomName, Short storageBegin, Short storageEnd) {
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询
        List<Room> empList=staffRoomMapper.list(building, roomName, storageBegin, storageEnd);
        Page<Room> p=(Page<Room>)empList;
        //3. 封装pageBean 对象
        PageBean pageBean=new PageBean(p.getResult(),p.getTotal());

        return pageBean;
    }
}
