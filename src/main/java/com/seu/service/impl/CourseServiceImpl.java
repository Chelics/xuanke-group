package com.seu.service.impl;

import com.seu.dto.response.FullFullCourse;
import com.seu.mapper.*;
import com.seu.pojo.FullCourse;
import com.seu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private CourseClassMapper courseClassMapper;
    @Autowired
    private RoomMapper roomMapper;
    //凡是需要联查的都需要用事件
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void getFullByBasic(FullCourse fullCourse) {
        //可复用
        Integer courseId=fullCourse.getId();
        //下面为避免Join联查，将联查分开

        //查询教师ID
        List<Integer> teacherIds = courseTeacherMapper.getTeacherIdsByCourseId(courseId);
        //根据教师ID查询名字
        List<String> teachers = teacherMapper.getNamesByIds(teacherIds);

        //查询班级ID
        List<Integer> classIds = courseClassMapper.getClassIdsByCourseId(courseId);
        //根据班级ID查询名字
        List<String> classes = classMapper.getNamesByIds(classIds);

        Integer roomId= fullCourse.getRoomId();
        //根据教室ID查询教室名
        String roomName=roomMapper.getRoomById(roomId);

        //补充返回的课程类
        fullCourse.setTeachers(teachers);
        fullCourse.setClasses(classes);
        fullCourse.setRoomName(roomName);

    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void getFullsByBasics(List<? extends FullCourse> fullCourses) {
        if (fullCourses == null || fullCourses.isEmpty()) {
            return;
        }
        for (FullCourse fullCourse : fullCourses) {
            getFullByBasic(fullCourse);
        }
    }
}
