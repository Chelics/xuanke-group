package com.seu.controller.teacherController;

import com.seu.pojo.FullCourse;
import com.seu.pojo.Result;
import com.seu.service.teacherService.TeacherCourseTableService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teacher/courseTable")
public class TeacherCourseTableController {
    @Autowired
    private TeacherCourseTableService tableService;
    @GetMapping
    public Result getCourseTable(@RequestAttribute Claims claims){
        Integer teacherId = (Integer) claims.get("id");
        log.info("根据ID查询课表,ID:{}",teacherId);
        List<FullCourse> courses= tableService.getCoursesByTeacherId(teacherId);
        return Result.success(courses);
    }
}
