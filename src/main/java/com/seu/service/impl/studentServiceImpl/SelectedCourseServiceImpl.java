package com.seu.service.impl.studentServiceImpl;

import com.seu.dto.response.FullFullCourse;
import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseStudentMapper;
import com.seu.service.impl.CourseServiceImpl;
import com.seu.service.studentService.SelectedCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SelectedCourseServiceImpl implements SelectedCourseService {

    @Autowired
    CourseStudentMapper courseStudentMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseServiceImpl courseService;
    @Override
    public List<FullFullCourse> getSelectedCourse(Integer studentId) {
        List<Integer> coursesIdsList = courseStudentMapper.getCourseIdsByStudentId(studentId);
        List<FullFullCourse> fullCourseList = courseMapper.getCoursesByIdss(coursesIdsList);
        courseService.getFullsByBasics(fullCourseList);
        for(FullFullCourse course : fullCourseList){
            Integer studentNum = courseStudentMapper.getStudentCountForCourse(course.getId());
            course.setStudentsNum(Objects.requireNonNullElse(studentNum, 0));
        }
        return fullCourseList;
    }
}
