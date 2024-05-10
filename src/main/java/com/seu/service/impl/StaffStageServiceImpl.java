package com.seu.service.impl;

import com.seu.mapper.StaffStageMapper;
import com.seu.pojo.Stage;
import com.seu.service.StaffStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffStageServiceImpl implements StaffStageService {
    @Autowired
    private StaffStageMapper staffStageMapper;
    @Override
    public List<Stage> list() {
        return staffStageMapper.list();
    }

    @Override
    public Stage getById(Integer id) {
        return staffStageMapper.getById(id);
    }

    @Override
    public void update(Stage stage) {
        staffStageMapper.update(stage);
    }

    @Override
    public Stage getTermName() {
        Stage stage=staffStageMapper.getTerName();
        return stage;
    }

    @Override
    public void updateTermName(Stage stage) {
        staffStageMapper.updateTermName(stage);
    }
}
