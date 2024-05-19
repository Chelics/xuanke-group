package com.seu.controller.studentController;

import com.seu.exception.InvalidInputException;
import com.seu.pojo.Result;
import com.seu.service.impl.studentServiceImpl.SearchCourseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student/course/search")
public class SearchCourseController {
    @Autowired
    SearchCourseServiceImpl searchCourseServiceImpl;

    @GetMapping
    public Result searchCoursesByKeyWord(@RequestParam String keyWord) throws InvalidInputException {

        if(keyWord == null || keyWord.trim().isEmpty()){
            throw new InvalidInputException("搜索关键词不能为空!");
        }

        log.info("搜索课程关键词: " + keyWord);
        return Result.success(searchCourseServiceImpl.searchCoursesByKeyWord(keyWord));
    }
}
