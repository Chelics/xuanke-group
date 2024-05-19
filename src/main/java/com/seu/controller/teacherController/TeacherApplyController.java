package com.seu.controller.teacherController;

import com.seu.pojo.CheckingCourse;
import com.seu.pojo.PageBean;
import com.seu.pojo.Result;
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
    @GetMapping
    public Result page(@RequestAttribute Claims claims, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize, Short status){
        switch (status) {
            case 1:
                log.info("分页查询待审核状态课程,参数:{},{}", page, pageSize);
            case 2:
                log.info("分页查询已通过状态课程,参数:{},{}", page, pageSize);
            case 3:
                log.info("分页查询被驳回状态课程,参数:{},{}", page, pageSize);
        }

        // 从claims中获取teacherId
        Integer teacherId = (Integer) claims.get("id");

        PageBean pageBean= teacherApplyService.page(teacherId,page,pageSize,status);
        return Result.success(pageBean);
    }
}
