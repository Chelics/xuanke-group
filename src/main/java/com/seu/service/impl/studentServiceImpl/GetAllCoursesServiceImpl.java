package com.seu.service.impl.studentServiceImpl;

import com.seu.mapper.CourseMapper;
import com.seu.pojo.FullCourse;
import com.seu.service.impl.CourseServiceImpl;
import com.seu.service.studentService.GetAllCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAllCoursesServiceImpl implements GetAllCoursesService {

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseServiceImpl courseService;

    /**
     * 获取所有课程
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public List<FullCourse> getAllCourse() {
        List<FullCourse> fullCourseList= courseMapper.list();
        courseService.getFullsByBasics(fullCourseList);
        return fullCourseList;
    }
}
