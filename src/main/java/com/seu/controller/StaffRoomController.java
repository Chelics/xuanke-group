package com.seu.controller;

import com.seu.pojo.Course;
import com.seu.pojo.PageBean;
import com.seu.pojo.Result;
import com.seu.pojo.Stage;
import com.seu.service.StaffRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/staff/rooms")
public class StaffRoomController {
    @Autowired
    private StaffRoomService staffRoomService;
    /**
     * 查询教室列表
     * @param page
     * @param pageSize
     * @param building
     * @param roomName
     * @param storageBegin
     * @param storageEnd
     * @return
     */
    @GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")Integer pageSize, String building, String roomName
            ,Short storageBegin,Short storageEnd){
        log.info("分页查询,参数:{},{},{},{},{},{}",page,pageSize,building,roomName,storageBegin,storageEnd);
        PageBean pageBean=staffRoomService.page(page,pageSize,building,roomName,storageBegin,storageEnd);
        return Result.success(pageBean);
    }

    /**
     * 根据ID查询该教室课表
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getCourseByRoomId(@PathVariable Integer id){
        log.info("根据ID查询课表,ID:{}",id);
        Course course= staffRoomService.getCourseByRoomId(id);
        return Result.success(course);
    }

}
