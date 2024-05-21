package com.seu.service.impl.teacherServiceImpl;

import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseTeacherMapper;
import com.seu.pojo.FullCourse;
import com.seu.service.CourseService;
import com.seu.service.teacherService.TeacherCourseTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherCourseTableServiceImpl implements TeacherCourseTableService {
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseService courseService;
    @Autowired
    CourseTeacherMapper courseTeacherMapper;


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public List<FullCourse> getCoursesByTeacherId(Integer teacherId) {
        List<Integer> courseIds= courseTeacherMapper.getCourseIdsByTeacherId(teacherId);
        List<FullCourse> fullCours=courseMapper.getCoursesByIds(courseIds);//仍是基本信息
        for (int i = 0; i < fullCours.size(); i++) {
            FullCourse fullCourse = fullCours.get(i);//对于每一个course
            //补充完整信息
            courseService.getFullByBasic(fullCourse);
        }
        return fullCours;
    }
}
