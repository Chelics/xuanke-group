package com.seu.controller.studentController;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.impl.studentServiceImpl.GetAllCoursesServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/course/get")
public class GetAllCoursesController {
    @Autowired
    GetAllCoursesServiceImpl getAllCourseService;
    /**
     * 获取所有课程信息
     * @return
     */
    @GetMapping
    public Result getAllCourses(@RequestAttribute Claims claims) throws InvalidInputException, EntityNotFoundException {

        Integer studentId;

        try {
            studentId = Integer.parseInt(claims.get("id").toString());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("非法的学生id");
        }
        if(studentId < 0){
            throw new InvalidInputException("非法的学生id");
        }
        return Result.success(getAllCourseService.getAllCourse(studentId));
    }
}
