package com.seu.controller;

import com.seu.pojo.PageBean;
import com.seu.pojo.Result;
import com.seu.service.StaffCheckingService;
import com.seu.service.StaffStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/staff/checking")
public class StaffCheckingController {
    @Autowired
    StaffCheckingService staffCheckingService;
    /**
     * 待审核列表分页查询
     * @return
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize,String courseName){
        log.info("分页查询,参数:{},{},{}",page,pageSize,courseName);
        PageBean pageBean=staffCheckingService.page(page,pageSize,courseName);
        return Result.success(pageBean);
    }
}
