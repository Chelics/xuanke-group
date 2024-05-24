package com.seu.controller.staffController;

import com.seu.dto.response.CourseTable;
import com.seu.pojo.*;
import com.seu.service.staffService.StaffRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<FullCourse> courses= staffRoomService.getCoursesByRoomId(id);
        CourseTable courseTable=new CourseTable(courses);
        return Result.success(courseTable);
    }

}
