package com.seu.controller.studentController;

import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.studentService.SelectedCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/course/selected")
public class SelectedCoursesController {

    @Autowired
    SelectedCourseService selectedCourseService;

    /**
     * 获取已选课程信息
     * @param userId
     * @return
     * @throws InvalidInputException
     */
    @GetMapping
    public Result getSelectedCourses(@RequestAttribute Integer userId) throws InvalidInputException {

        Integer studentId = userId;

        log.info(studentId + " : 获取已选课表");
        return Result.success(selectedCourseService.getSelectedCourse(studentId));
    }
}
