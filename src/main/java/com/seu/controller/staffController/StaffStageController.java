package com.seu.controller.staffController;

import com.seu.pojo.Result;
import com.seu.pojo.Stage;
import com.seu.service.staffService.StaffStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/staff/stages")
public class StaffStageController {
    @Autowired
    private StaffStageService staffStageService;

    /**
     * 查询全部阶段信息
     * @return
     */
    @GetMapping
    public Result list(){
        log.info("查询全部阶段信息");
        List<Stage> stages= staffStageService.list();
        return Result.success(stages);
    }

    /**
     * 根据ID查询阶段
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据ID查询阶段,ID:{}",id);
        Stage stage= staffStageService.getById(id);
        return Result.success(stage);
    }

    /**
     * 修改阶段
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Stage stage){
        log.info("修改阶段,{}",stage);
        staffStageService.update(stage);
        return Result.success();
    }

    /**
     * 回显学期名
     * @return
     */
    @GetMapping("/term")
    public Result getTermName(){
        log.info("回显学期名");
        Stage stage=staffStageService.getTermName();
        return Result.success(stage);
    }

    /**
     * 修改学期名
     * @return
     */
    @PutMapping("/term")
    public Result updateTermName(@RequestBody Stage stage){
        staffStageService.updateTermName(stage);
        return Result.success();
    }

}
