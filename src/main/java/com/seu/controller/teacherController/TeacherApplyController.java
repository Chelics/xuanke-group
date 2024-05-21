package com.seu.controller.teacherController;

import com.seu.dto.response.ClassList;
import com.seu.dto.response.TeacherList;
import com.seu.pojo.CheckingCourse;
import com.seu.pojo.PageBean;
import com.seu.pojo.Result;
import com.seu.pojo.Users.Teacher;
import com.seu.service.teacherService.TeacherApplyService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/teacher/apply")
public class TeacherApplyController {
    @Autowired
    private TeacherApplyService teacherApplyService;

    /**
     * 根据教师名模糊查询教师
     * @return
     */
    @GetMapping("/teachers")
    public Result getByteacherName(String teacherName){
        log.info("模糊查询教师,参数:{}",teacherName);
        TeacherList teacherList=teacherApplyService.getByteacherName(teacherName);
        return Result.success(teacherList);
    }

    /**
     * 根据班级名模糊查询班级
     * @return
     */
    @GetMapping("/classes")
    public Result getByclassName(String className){
        log.info("模糊查询班级,参数:{}",className);
        ClassList classList=teacherApplyService.getByclassName(className);
        return  Result.success(classList);
    }

    /**
     * 申报课程
     */
    @PostMapping
    public Result applyCourse(@RequestBody CheckingCourse applyingCourse){
        teacherApplyService.applyCourse(applyingCourse);
        return Result.success();
    }

    /**
     * 查询未审核/已通过/已驳回状态课程
     * @return
     */
    @GetMapping("/{status}")
    public Result page(@RequestAttribute Claims claims, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize,@PathVariable Short status){

        log.info("分页查询待审核状态课程,参数:{},{},{}", page, pageSize,status);
        // 从claims中获取teacherId
        Integer teacherId = (Integer) claims.get("id");

        PageBean pageBean = teacherApplyService.page(teacherId, page, pageSize, status);
        return Result.success(pageBean);
    }
}
