package com.seu.controller;

import com.seu.pojo.PageBean;
import com.seu.pojo.Result;
import com.seu.service.StaffRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
