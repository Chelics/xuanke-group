package com.seu.controller.staffController;


import com.seu.exception.AllocateCourseException;
import com.seu.dto.request.CheckingChoice;
import com.seu.pojo.FullCheckingCourse;
import com.seu.pojo.PageBean;
import com.seu.pojo.Result;
import com.seu.service.staffService.StaffCheckingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/staff/checking")
public class StaffCheckingController {
    @Autowired
    StaffCheckingService staffCheckingService;
    /**
     * 待审核列表分页查询
     * @return
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize,String courseName){
        log.info("分页查询,参数:{},{},{}",page,pageSize,courseName);
        PageBean pageBean=staffCheckingService.page(page,pageSize,courseName);
        return Result.success(pageBean);
    }

    /**
     * 批量通过或驳回
     * @param choice
     * @return
     */
    @PutMapping
    public Result choose(@RequestBody CheckingChoice choice) throws AllocateCourseException {
        Integer status=choice.getStatus();
        List<Integer> ids=choice.getIds();
        if(status==2){//通过
            log.info("通过请求,id:{}",ids);
            staffCheckingService.pass(ids);
        }
        else if(status==3){//驳回
            log.info("驳回请求,id:{}",ids);
            staffCheckingService.reject(ids);

        }
        else{
            return Result.error("审核状态出错了");
        }
        return Result.success();
    }

    /**
     * 根据ID查询待审核项
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        FullCheckingCourse full=staffCheckingService.getById(id);
        return Result.success(full);
    }
}
