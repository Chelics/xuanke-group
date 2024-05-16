package com.seu.service.impl;

import com.seu.mapper.StageMapper;
import com.seu.pojo.Stage;
import com.seu.service.StaffStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffStageServiceImpl implements StaffStageService {
    @Autowired
    private StageMapper stageMapper;
    @Override
    public List<Stage> list() {
        return stageMapper.list();
    }

    @Override
    public Stage getById(Integer id) {
        return stageMapper.getById(id);
    }

    @Override
    public void update(Stage stage) {
        stageMapper.update(stage);
    }

    @Override
    public Stage getTermName() {
        Stage stage= stageMapper.getTerName();
        return stage;
    }

    @Override
    public void updateTermName(Stage stage) {
        stageMapper.updateTermName(stage);
    }
}
