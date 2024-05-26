package com.seu.service.impl.studentServiceImpl;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.exception.SelectCourseException;
import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseStudentMapper;
import com.seu.service.CheckStageService;
import com.seu.service.studentService.DropCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DropCourseServiceImpl implements DropCourseService {
    @Autowired
    CourseStudentMapper courseStudentMapper;
    @Autowired
    CheckStageService checkStageService;
    @Autowired
    CourseMapper courseMapper;

    /**
     * 退课操作
     * @param courseId
     * @param studentId
     * @return
     */
    @Transactional
    @Override
    public boolean dropCourse(Integer courseId, Integer studentId) throws InvalidInputException, EntityNotFoundException, SelectCourseException {

        checkStageService.checkStage();

        if(courseStudentMapper.deleteByStudentIdAndCourseId(courseId, studentId) > 0){
            courseMapper.decrementStudentNum(courseId);
            return true;
        }else{
            throw new InvalidInputException("未选择该课程");
        }
    }
}
