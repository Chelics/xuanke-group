package com.seu.service;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.SelectCourseException;

public interface CheckStageService {
    void checkStage() throws SelectCourseException, EntityNotFoundException;
}
