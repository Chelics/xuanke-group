package com.seu.service;

import com.seu.pojo.Stage;

import java.util.List;

public interface StaffStageService {
    /**
     * 查询全部阶段
     * @return
     */
    List<Stage> list();

    /**
     * 根据ID查询阶段
     * @param id
     * @return
     */
    Stage getById(Integer id);

    /**
     * 修改阶段
     * @param stage
     */
    void update(Stage stage);

    /**
     * 回显学期名
     * @return
     */
    Stage getTermName();

    /**
     * 修改学期名
     * @param stage
     */
    void updateTermName(Stage stage);
}
