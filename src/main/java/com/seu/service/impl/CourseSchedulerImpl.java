package com.seu.service.impl;

import com.seu.config.SchedulerConfig;
import com.seu.exception.AllocateCourseException;
import com.seu.exception.EntityNotFoundException;
import com.seu.exception.InvalidInputException;
import com.seu.mapper.*;
import com.seu.pojo.CheckingCourse;
import com.seu.pojo.Course;
import com.seu.pojo.FullCourse;
import com.seu.service.CourseScheduler;
import com.seu.utils.CheckTimeConflictsUtils;
import com.seu.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CourseSchedulerImpl implements CourseScheduler {

    /***********************************************************************************
     * 前面这一大段都是用来从配置类中读取常量的
     */

    private final SchedulerConfig schedulerConfig;  //scheduler配置类, 用于从配置文件中读取配置信息
    @Autowired
    public CourseSchedulerImpl(SchedulerConfig schedulerConfig){    //配置类, 用于读取配置文件中的内容, 并转交给业务类
        this.schedulerConfig = schedulerConfig;
    }
    public short getCourseNumsOneDay(){     //8, 每天的节次数
        return schedulerConfig.getCourseNumsOneDay();
    }
    public short getNullTime() {    //-1, 表示空时间
        return schedulerConfig.getNullTime();
    }

    public short[][] getWeekRange() {   //[[1, 111], [112, 167]]节次编码范围, 隔周上课选第一组, 每周上课选第二组
        return schedulerConfig.getWeekRange();
    }

    public short[][] getTimeList() {    //[[0, 1, 3, 4, 6], [2, 5, 7]] 节次列表, 上两节选第一组, 上三节选第二组
        return schedulerConfig.getTimeList();
    }

    public Map<Short, Integer[]> getTimeClassificationMap() {   //哈希表, 可以去配置文件scheduler-config.yml看详细的解释
        return schedulerConfig.getTIME_CLASSIFICATION_MAP_SHORT();
    }

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    CheckingMapper checkingMapper;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    CourseClassMapper courseClassMapper;
    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    /**************************************************************************************
     * 为多个课程分配时间和教室
     * @param courseIds 课程ID列表
     * @return 分配结果
     */
    public List<Integer> allocateMultipleCourses(List<Integer> courseIds) throws AllocateCourseException {
        List<Integer> successfulCourses = new ArrayList<>();    //用于存储分配成功的课程列表, 暂时没有用处, 之后需求变更可能会用到
        List<Integer> failedCourses = new ArrayList<>();    //分配失败的课程列表

        for (Integer courseId : courseIds) {
            try {
                allocateTimeAndRoom(courseId);      //对单个课程进行排课
                log.info("分配时间和教室成功: " + courseId);
                successfulCourses.add(courseId);    //成功
            } catch (EntityNotFoundException | InvalidInputException | AllocateCourseException e) {    //失败
                log.error("分配时间或教室失败: " + courseId +e.getMessage());
                failedCourses.add(courseId);
            }
        }
            return failedCourses;   //返回失败课程列表
        }

    /**
     * 为单个通过审核的课程分配空闲的时间和教室
     * @param id
     * @throws EntityNotFoundException
     * @throws InvalidInputException
     * @throws AllocateCourseException
     */
    @Override
    public synchronized void allocateTimeAndRoom(Integer id) throws EntityNotFoundException, InvalidInputException, AllocateCourseException {

        //调取待分配课程
        CheckingCourse checkingCourse = checkingMapper.getById(id);

        //先找时间
        short[] times = classifyTime(id, checkingCourse);

        //再找教室
        Integer roomId = getFreeRoom(id, times, checkingCourse);

        //加入数据库
        putInStorage(checkingCourse, times, roomId);

    }

    /**
     * 分配时间第一步: 根据课时数对课程所需时间进行分类
     * @param id
     * @return
     * @throws EntityNotFoundException
     * @throws InvalidInputException
     * @throws AllocateCourseException
     */
    @Override
    public short[] classifyTime(Integer id, CheckingCourse checkingCourse) throws EntityNotFoundException, InvalidInputException, AllocateCourseException {

        if (checkingCourse == null) {
            throw new EntityNotFoundException("未找到待分配课程");
        }

        Short timeInNeed = checkingCourse.getCourseHour();  //所需课时数

        Integer[] listChoices = getTimeClassificationMap().get(timeInNeed);     //从配置文件的哈希表中读取对应的listChoices
        if(listChoices == null){    //哈希表没读到
            throw new InvalidInputException("课时数不符合自动分配条件");
        }

        int rangeChoice = timeInNeed <= 24 ? 0 : 1;     //隔周上课 / 每周上课

        return searchFreeTime(rangeChoice, listChoices, checkingCourse);    //进入第二步
    }


    /**
     * 分配时间第二步: 找到满足条件的空闲时间
     * @param rangeChoice
     * @param listChoices
     * @param checkingCourse
     * @return
     * @throws AllocateCourseException
     */
    private short[] searchFreeTime(int rangeChoice, Integer[] listChoices, CheckingCourse checkingCourse) throws AllocateCourseException {

        final short NULL_TIME = getNullTime();      //-1
        final short[][] WEEK_RANGE = getWeekRange();    //[[1, 111], [112, 167]]
        final short[][] COURSE_NUMS = getTimeList();    //[[0, 1, 3, 4, 6], [2, 5, 7]]
        final short COUR_NUMS_ONE_DAY = getCourseNumsOneDay();  //8

        short[] timeList = {NULL_TIME, NULL_TIME, NULL_TIME};    //存储time1, time2, time3, -1表示null

        for (int i = 0; i < listChoices.length; i++) {  //listChoices的长度表示每周上几次课, 上几次就要选几次

            for(short time = WEEK_RANGE[rangeChoice][0]; time <= WEEK_RANGE[rangeChoice][1]; time++){   //在隔周/每周范围内遍历所有时间
                if(Arrays.binarySearch(COURSE_NUMS[listChoices[i]], (short) (time % COUR_NUMS_ONE_DAY)) >= 0){ //找到符合课时数的时间(2节/3节)
                    if( !isClassFree(time, checkingCourse) && !isTeacherFree(time, checkingCourse)){    //班级和教师都空闲

                        //检查是不是刚才已经被自己选了
                        boolean isTimeAlreadyAssigned = false;
                        for (short assignedTime : timeList) {
                            if (assignedTime == time) {     // timeList中已存在该时间
                                isTimeAlreadyAssigned = true;
                                break;
                            }
                        }
                        if (!isTimeAlreadyAssigned) {
                            timeList[i] = time;     //选择该时间
                            break;
                        }
                    }
                }
            }
            if(NULL_TIME == timeList[i]){   //没选上
                throw new AllocateCourseException("没有空闲的上课时间可供分配");
            }
        }

        return timeList;    //时间分配成功, 返回分配的时间列表
    }

    /**
     * 分配时间调用此方法: 检查班级是否空闲
     * @param time
     * @param checkingCourse
     * @return
     */
    public boolean isClassFree(short time, CheckingCourse checkingCourse){
        List<Integer> classIds = IdUtils.stringToList(checkingCourse.getClassIds());
        List<Integer> courseIds = courseClassMapper.getCourseIdsByClassIds(classIds);
        List<FullCourse> courseList = courseMapper.getCoursesByIds(courseIds);
        return (CheckTimeConflictsUtils.checkCoursesAndTime(courseList, time));
    }

    /**
     * 分配时间调用此方法: 检查教师是否空闲
     * @param time
     * @param checkingCourse
     * @return
     */
    public boolean isTeacherFree(short time, CheckingCourse checkingCourse){
        List<Integer> teacherList = IdUtils.stringToList(checkingCourse.getTeacherIds());
        List<Integer> courseIds = courseTeacherMapper.getCoursesByTeachers(teacherList);
        List<FullCourse> courseList = courseMapper.getCoursesByIds(courseIds);
        return (CheckTimeConflictsUtils.checkCoursesAndTime(courseList, time));
    }

    /**
     * 获取空闲教室
     * @param id
     * @param times
     * @param checkingCourse
     * @return
     * @throws AllocateCourseException
     */
    @Override
    public Integer getFreeRoom(Integer id, short[] times, CheckingCourse checkingCourse) throws AllocateCourseException {
        //每次将一座教学楼中符合容量所有教室全都读进来, 没找到再读下一座楼
        for(int buildingCode = 1; buildingCode < 9; buildingCode++){
            String building = "教" + buildingCode;
            Short storage = checkingCourse.getCourseStorage();

            List<Integer> roomIds = roomMapper.idList(building, null, storage, storage);

            for(Integer roomId : roomIds){
                List<FullCourse> courseList = courseMapper.getCoursesByRoomId(roomId);  //使用该教室的全部课程
                if(!CheckTimeConflictsUtils.checkCoursesAndTimes(courseList, times)){   //所有课程都不与待分配课程所选的时间冲突
                    return roomId;  //分配该教室
                }
            }
        }
        //遍历完还没找到
        throw new AllocateCourseException("未找到空闲的教室!");
    }

    /**
     * 将分配好时间和教室的课程入库
     * @param checkingCourse
     * @param times
     * @param roomId
     */
    private void putInStorage(CheckingCourse checkingCourse, short[] times, Integer roomId) throws AllocateCourseException {
        Course course = new Course(null, checkingCourse.getCourseName(), checkingCourse.getType(),
                checkingCourse.getCourseNumber(), roomId, checkingCourse.getCourseHour(), checkingCourse.getCourseStorage(),
                (short) 1, (short) 16, times[0], times[1], times[2], checkingCourse.getFaculty(), checkingCourse.getCredit());

        //插入course表
        courseMapper.insertCourse(course);
        Integer courseId = course.getId();  //无论是否有相同的课程编号, 都会返回表中对应编号的id
        log.info("新增courseId: " + courseId);

        if(courseId == null){
            throw new AllocateCourseException("数据库course表写入失败");
        }

        //插入course_teacher表
        for(Integer teacherId : IdUtils.stringToList(checkingCourse.getTeacherIds())){
            courseTeacherMapper.insertCourseTeacher(courseId, teacherId);
        }
        //插入course_class表
        for(Integer classId : IdUtils.stringToList(checkingCourse.getClassIds())){
            courseClassMapper.insertCourseClass(courseId, classId);
        }
    }
}
