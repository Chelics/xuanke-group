package com.seu.controller.studentController;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.impl.studentServiceImpl.StudentStageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/stage")
public class StudentStageController {
    @Autowired
    StudentStageServiceImpl getStageService;

    /**
     * 根据阶段id获取阶段信息
     * @param id
     * @return
     * @throws InvalidInputException
     * @throws EntityNotFoundException
     */
    @GetMapping
    public Result getStage(@RequestParam Integer id) throws InvalidInputException, EntityNotFoundException {
        log.info("获取阶段信息");

        if(id == null || id < 0){
            throw new InvalidInputException("非法的学期id");
        }

        return Result.success(getStageService.getStage(id));
    }
}
