package com.seu.service.impl;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.mapper.CheckingMapper;
import com.seu.mapper.CourseMapper;
import com.seu.mapper.RoomMapper;
import com.seu.pojo.CheckingCourse;
import com.seu.service.CourseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Slf4j
@Service
public class CourseSchedulerImpl implements CourseScheduler {

    static public final int[][] WEEKRANGE = {{1, 111}, {112, 167}};
    static public final int[][] TIMELIST = {{0, 1, 3, 4, 6}, {2, 5, 7}};

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    CheckingMapper checkingMapper;
    @Override
    public void allocateTimeAndRoom(Integer id) throws EntityNotFoundException, InvalidInputException {
        classifyTime(id);
        allocateRoom(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void classifyTime(Integer id) throws EntityNotFoundException, InvalidInputException {
        CheckingCourse checkingCourse = checkingMapper.getById(id);

        if (checkingCourse == null) {
            throw new EntityNotFoundException("未找到待分配课程");
        }

        int rangeChoice;
        int listChoice;

        Short timeInNeed = checkingCourse.getCourseHour();
        if(timeInNeed <= 24){
            rangeChoice = 0;
            if(timeInNeed <= 16){
                listChoice = 0;
            }else{
                listChoice = 1;
            }
        }else{
            rangeChoice = 1;
            switch(timeInNeed){
                case 32:

                    break;
                case 48:

                    break;
                case 64:

                    break;
                case 80:

                    break;
                case 96:

                    break;

                default:
                    throw new InvalidInputException("课时数不符合自动分配条件");
            }

        }
    }

    @Override
    public void allocateRoom(Integer id) {

    }

    private void allocateTime(int rangeChoice, int listChoice, CheckingCourse checkingCourse){
        for(int i : WEEKRANGE[rangeChoice]){
            if(Arrays.binarySearch(TIMELIST[listChoice], (i / 21)) > 0){


            }
        }
    }
}
