package com.seu.controller.studentController;

import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.studentService.SelectedCourseService;
import io.jsonwebtoken.Claims;
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
     * @param claims
     * @return
     * @throws InvalidInputException
     */
    @GetMapping
    public Result getSelectedCourses(@RequestAttribute Claims claims) throws InvalidInputException {

        // 从claims中获取studentId
        String studentIdStr = claims.get("id").toString();

        // 将studentIdStr转换为整数
        Integer studentId;
        try {
            studentId = Integer.parseInt(studentIdStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("获取失败: 非法的学生id");
        }

        if(studentId < 0){
            throw new InvalidInputException("获取失败: 非法的学生id");
        }

        log.info(studentId + " : 获取已选课表");
        return Result.success(selectedCourseService.getSelectedCourse(studentId));
    }
}
