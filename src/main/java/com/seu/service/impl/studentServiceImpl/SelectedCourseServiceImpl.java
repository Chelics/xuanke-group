package com.seu.service.impl.studentServiceImpl;

import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseStudentMapper;
import com.seu.pojo.FullCourse;
import com.seu.service.impl.CourseServiceImpl;
import com.seu.service.studentService.SelectedCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectedCourseServiceImpl implements SelectedCourseService {

    @Autowired
    CourseStudentMapper courseStudentMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseServiceImpl courseService;
    @Override
    public List<FullCourse> getSelectedCourse(Integer studentId) {
        List<Integer> coursesIdsList = courseStudentMapper.getCourseIdsByStudentId(studentId);
        List<FullCourse> fullCourseList = courseMapper.getCoursesByIds(coursesIdsList);
        courseService.getFullsByBasics(fullCourseList);

        return fullCourseList;
    }
}
