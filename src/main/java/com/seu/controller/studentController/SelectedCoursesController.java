package com.seu.controller.studentController;

import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.studentService.SelectedCourseService;
import com.seu.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/student/course/selected")
public class SelectedCoursesController {

    @Autowired
    SelectedCourseService selectedCourseService;
    @GetMapping
    public Result getSelectedCourses(HttpServletRequest request) throws InvalidInputException {
        //从请求头拿到JWT令牌
        String jwtToken = request.getHeader("Authorization");

        //解析令牌, 获取claims
        Claims claims = JwtUtils.parseJWT(jwtToken.replace("Bearer", ""));

        // 从claims中获取studentId
        String studentIdStr = claims.get("id").toString();

        // 将studentIdStr转换为整数
        Integer studentId;
        try {
            studentId = Integer.parseInt(studentIdStr);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("获取失败: 非法的学生id");
        }

        if(studentId == null || studentId < 0){
            throw new InvalidInputException("获取失败: 非法的学生id");
        }

        log.info(studentId + " : 获取已选课表");
        return Result.success(selectedCourseService.getSelectedCourse(studentId));
    }
}
