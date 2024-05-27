package com.seu.service.impl.studentServiceImpl;

import com.seu.exception.EntityNotFoundException;
import com.seu.mapper.CourseClassMapper;
import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseStudentMapper;
import com.seu.mapper.StudentMapper;
import com.seu.pojo.FullCourse;
import com.seu.pojo.Users.Student;
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
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CourseClassMapper courseClassMapper;
    @Autowired
    CourseStudentMapper courseStudentMapper;

    /**
     * 获取所有课程
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public List<FullCourse> getAllCourse(Integer studentId) throws EntityNotFoundException {

        Student student = studentMapper.getStudentById(studentId);
        if(student == null){
            throw new EntityNotFoundException("未查询到学生信息");
        }
        Integer classId = student.getClassId();
        if(classId == null || classId < 0){
            throw new EntityNotFoundException("未查询到班级信息");
        }

        List<Integer> courseIds = courseClassMapper.getCoursesByClassId(classId);
        List<FullCourse> courseList = courseMapper.getCoursesByIds(courseIds);

        //添加通选课到课程列表
        List<FullCourse> universalCourseList = courseMapper.getUniversalCourses();
        if(universalCourseList != null && !universalCourseList.isEmpty()){
            List<FullCourse> nonDuplicateUniversalCourses = universalCourseList.stream()
                    .filter(uc -> !courseList.contains(uc))
                    .toList();

            courseList.addAll(nonDuplicateUniversalCourses);
        }

        courseService.getFullsByBasics(courseList);

        return courseList;
    }
}
