package com.seu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seu.mapper.*;
import com.seu.pojo.*;
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
    private CourseTeacherMapper courseTeacherMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private CourseClassMapper courseClassMapper;
    @Override
    public PageBean page(Integer page, Integer pageSize, String building, String roomName, Short storageBegin, Short storageEnd) {
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询
        List<Room> empList= roomMapper.list(building, roomName, storageBegin, storageEnd);
        Page<Room> p=(Page<Room>)empList;
        //3. 封装pageBean 对象
        PageBean pageBean=new PageBean(p.getResult(),p.getTotal());

        return pageBean;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public List<FullCourse> getCoursesByRoomId(Integer id) {
        List<FullCourse> fullCours = courseMapper.getCoursesByRoomId(id);//子类强转父类
        for (int i = 0; i < fullCours.size(); i++) {
            FullCourse fullCourse = fullCours.get(i);//对于每一个course
            //获取教室ID
            int courseId = fullCourse.getId();

            //下面为避免Join联查，将联查分开

            //查询教师ID
            List<Integer> teacherIds = courseTeacherMapper.getTeacherIdsByCourseId(courseId);
            //根据教师ID查询名字
            List<String> teachers = teacherMapper.getNamesByIds(teacherIds);

            //查询班级ID
            List<Integer> classIds = courseClassMapper.getClassIdsByCourseId(courseId);
            //根据班级ID查询名字
            List<String> classes = classMapper.getNamesByIds(classIds);
            //补充返回的课程类
            fullCourse.setTeachers(teachers);
            fullCourse.setClasses(classes);

        }
        return fullCours;
    }
}
