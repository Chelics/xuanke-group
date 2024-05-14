package com.seu.controller;

import com.seu.pojo.Result;
import com.seu.service.StaffStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("staff/checking")
public class StaffCheckingController {

    /**
     * 待审核列表查询
     * @return
     */
    @GetMapping
    public Result list(){

        return null;
    }
}
