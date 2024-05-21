package com.seu.controller.studentController;

import com.seu.dto.request.CourseSelection;
import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.exception.SelectCourseException;
import com.seu.pojo.Result;
import com.seu.service.studentService.SelectCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/course/select")
public class SelectCourseController {
    @Autowired
    SelectCourseService studentSelectCourseService;

    /**
     * 学生选课
     * @param courseSelection
     * @return
     * @throws InvalidInputException
     * @throws SelectCourseException
     */
    @PostMapping
    public Result selectCourse(@RequestBody CourseSelection courseSelection) throws InvalidInputException, SelectCourseException, EntityNotFoundException {
        Integer courseId = courseSelection.getCourseId();
        Integer studentId = courseSelection.getStudentId();

        //检查输入
        if(courseId == null || courseId < 0 || studentId == null || studentId < 0) {
            throw new InvalidInputException("选课失败: 课程id或学生id为空");
        }
        log.info("学生选课: " + studentId + " 选: " + courseId);

        if(studentSelectCourseService.selectCourse(courseId, studentId)){
            log.info("选课成功: " + studentId + " 选: " + courseId);
            return Result.success("选课成功");
        }else{
            throw new SelectCourseException("未知原因", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
