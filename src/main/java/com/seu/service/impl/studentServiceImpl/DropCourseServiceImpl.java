package com.seu.service.impl.studentServiceImpl;

import com.seu.exception.DropCourseFailureException;
import com.seu.mapper.CourseStudentMapper;
import com.seu.service.studentService.DropCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DropCourseServiceImpl implements DropCourseService {
    @Autowired
    CourseStudentMapper courseStudentMapper;

    /**
     * 退课操作
     * @param courseId
     * @param studentId
     * @return
     */
    @Override
    public boolean dropCourse(Integer courseId, Integer studentId) throws DropCourseFailureException {
        //退课
        int result = courseStudentMapper.deleteByStudentIdAndCourseId(courseId, studentId);

        if(result > 0){
            return true;
        }else{
            throw new DropCourseFailureException("未选择该课程");
        }
    }
}
