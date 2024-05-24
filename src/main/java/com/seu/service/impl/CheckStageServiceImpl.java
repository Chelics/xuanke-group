package com.seu.service.impl;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.SelectCourseException;
import com.seu.mapper.StageMapper;
import com.seu.pojo.Stage;
import com.seu.service.CheckStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CheckStageServiceImpl implements CheckStageService {

    @Autowired
    StageMapper stageMapper;
    @Override
    public void checkStage() throws SelectCourseException, EntityNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        Stage stage = stageMapper.getCurrStage(now);

        if(stage == null){
            throw new EntityNotFoundException("未知的选课阶段: " + now);
        }

        String stageName = stage.getStageName();
        if(stageName.contains("-开放选课") || stageName.contains("退改补")){
            return;
        }

        if (stageName.contains("未开放")) {
            throw new SelectCourseException("选课未开始", HttpStatus.BAD_REQUEST);
        }else if(stageName.contains("结束")){
            throw new SelectCourseException("选课已结束", HttpStatus.BAD_REQUEST);
        }else{
            throw new SelectCourseException("未知的选课阶段: " + stageName, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
