package com.seu.service.impl.studentServiceImpl;

import com.seu.exception.EntityNotFoundException;
import com.seu.mapper.StageMapper;
import com.seu.pojo.Stage;
import com.seu.service.studentService.StudentStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentStageServiceImpl implements StudentStageService {
    @Autowired
    StageMapper stageMapper;

    @Override
    public Stage getStage(Integer id) throws EntityNotFoundException {

        Stage stage =  stageMapper.getById(id);
        if(stage == null){
            throw new EntityNotFoundException("选课阶段不存在");
        }
        return stage;
    }
}
