package com.seu.service.studentService;

import com.seu.exception.EntityNotFoundException;
import com.seu.pojo.Stage;

public interface StudentStageService {
    Stage getStage(Integer id) throws EntityNotFoundException;
}
