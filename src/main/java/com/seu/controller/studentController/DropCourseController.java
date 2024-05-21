package com.seu.controller.studentController;

import com.seu.dto.request.CourseSelection;
import com.seu.exception.DropCourseFailureException;
import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.impl.studentServiceImpl.DropCourseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/course/drop")
public class DropCourseController {
    @Autowired
    DropCourseServiceImpl studentDropCourse;

    /**
     * 退课
     * @param courseSelection
     * @return
     * @throws InvalidInputException
     * @throws DropCourseFailureException
     */
    @PostMapping
    public Result dropCourse(@RequestBody CourseSelection courseSelection) throws InvalidInputException, DropCourseFailureException {
        Integer courseId = courseSelection.getCourseId();
        Integer studentId = courseSelection.getStudentId();

        if(courseId == null || courseId < 0|| studentId == null || studentId < 0){
            throw new InvalidInputException("课程或学生id错误");
        }

        log.info("学生退课 " + studentId + " 退选: " + courseId);

        studentDropCourse.dropCourse(courseId, studentId);

        log.info("学生退课成功 " + studentId + " 退选: " + courseId);
        return Result.success("退课成功!");
    }
}
