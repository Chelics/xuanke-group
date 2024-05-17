package com.seu.controller;

import com.seu.pojo.ApplyingCourse;
import com.seu.pojo.PageBean;
import com.seu.pojo.Result;
import com.seu.service.TeacherCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teacher/apply")

public class TeacherCourseController {
    @Autowired
    private TeacherCourseService teacherCourseService;

    /**
     * 申报课程
     */
    @GetMapping
    public void applyCourse(ApplyingCourse applyingCourse){

    }

    /**
     * 查询未审核状态课程
     * @return
     */
    @GetMapping
    public Result pageNotReviewed(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize){
        log.info("分页查询未审核状态课程,参数:{},{}",page,pageSize);
        PageBean pageBean=teacherCourseService.pageNotReviewed(page,pageSize);
        return Result.success(pageBean);
    }

    /**
     * 查询已通过状态课程
     * @return
     */
    @GetMapping
    public Result pagePassed(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize){
        log.info("分页查询已通过状态课程,参数:{},{}",page,pageSize);
        PageBean pageBean=teacherCourseService.pagePassed(page,pageSize);
        return Result.success(pageBean);
    }

    /**
     * 查询已驳回状态课程
     * @return
     */
    @GetMapping
    public Result pageRejected(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize){
        log.info("分页查询已驳回状态课程,参数:{},{}",page,pageSize);
        PageBean pageBean=teacherCourseService.pageRejected(page,pageSize);
        return Result.success(pageBean);
    }
}
