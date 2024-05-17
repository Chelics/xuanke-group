package com.seu.controller.studentController;

import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.studentService.SelectedCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/course/selected")
public class SelectedCoursesController {

    @Autowired
    SelectedCourseService selectedCourseService;
    @GetMapping
    public Result getSelectedCourses(@RequestParam Integer studentId) throws InvalidInputException {

        if(studentId == null || studentId < 0){
            throw new InvalidInputException("获取失败: 非法的学生id");
        }

        log.info(studentId + " : 获取已选课表");
        return Result.success(selectedCourseService.getSelectedCourse(studentId));
    }
}
