package com.seu.controller.studentController;

import com.seu.pojo.Result;
import com.seu.service.impl.studentServiceImpl.GetAllCoursesServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/course/get")
public class GetAllCoursesController {
    @Autowired
    GetAllCoursesServiceImpl getAllCourseService;

    @GetMapping
    public Result getAllCourses(){
        return Result.success(getAllCourseService.getAllCourse());
    }
}
