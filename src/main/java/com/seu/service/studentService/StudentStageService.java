package com.seu.service.studentService;

import com.seu.exception.EntityNotFoundException;
import com.seu.pojo.Stage;

public interface StudentStageService {
    /**
     * 根据阶段id获取阶段信息
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    Stage getStage(Integer id) throws EntityNotFoundException;
}
