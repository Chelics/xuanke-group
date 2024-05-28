package com.seu.service.impl.studentServiceImpl;

import com.seu.exception.EntityNotFoundException;
import com.seu.exception.SelectCourseException;
import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseStudentMapper;
import com.seu.mapper.StageMapper;
import com.seu.mapper.StudentMapper;
import com.seu.pojo.FullCourse;
import com.seu.pojo.Users.Student;
import com.seu.service.CheckStageService;
import com.seu.service.studentService.SelectCourseService;
import com.seu.utils.CheckTimeConflictsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service
@Slf4j
public class SelectCourseServiceImpl implements SelectCourseService {

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CourseStudentMapper courseStudentMapper;
    @Autowired
    StageMapper stageMapper;
    @Autowired
    CheckStageService checkStageService;

    //锁哈希表, 为每个课程id储存锁
    private final ConcurrentHashMap<Integer, Lock> lockMap = new ConcurrentHashMap<>();

    /**
     * 学生选课
     *
     * @param courseId
     * @param studentId
     */
    @Override
    public boolean selectCourse(Integer courseId, Integer studentId) throws SelectCourseException, EntityNotFoundException {

        FullCourse fullCourse = courseMapper.getCourseById(courseId);

        //输入检查 & 时间冲突检查
        validateInput(fullCourse, studentId);

        Lock lock = lockMap.computeIfAbsent(courseId, k -> new ReentrantLock());
        lock.lock();
        try {
            //检查课程容量是否已满
            checkStorage(courseId, fullCourse);
            log.info("已完成课程容量检查, 课程容量未满");
            //向关联表中插入记录
            insertSelectedCourse(courseId, studentId);
        } finally {
            lock.unlock();
        }

        log.info("学生{}成功选课{}, 成功向数据库中插入数据", studentId, courseId);
        return true;
    }

    /**
     * 确认学生和课程都存在, 且待选课程与已选课程没有时间冲突
     * @param fullCourse
     * @param studentId
     * @return
     */
    private void validateInput(FullCourse fullCourse, Integer studentId) throws SelectCourseException, EntityNotFoundException {

        //判断是否处在可以选课的阶段
        checkStageService.checkStage();
        log.info("当前选课阶段可以选课");

        if (fullCourse == null) {
            log.warn("未找到id对应的课程");
            throw new SelectCourseException("课程不存在", HttpStatus.BAD_REQUEST);
        }
        log.info("已找到id对应的课程");
        Student student = studentMapper.getStudentById(studentId);
        if(student == null){
            log.warn("未找到id对应的学生");
            throw new SelectCourseException("学生不存在: " + studentId, HttpStatus.BAD_REQUEST);
        }
        log.info("已找到id对应的学生");
        if (hasTimeConflicts(studentId, fullCourse)){
            log.warn("检查到课程时间冲突: {}", fullCourse.getCourseName());
            throw new SelectCourseException("课程已选或与其它已选课程存在时间冲突", HttpStatus.BAD_REQUEST);
        }
        log.info("没有检查到课程时间冲突");

    }

    /**
     * 检查待选课程是否与已选课程存在时间冲突
     * @param studentId
     * @param fullCourse
     * @return
     */
    private boolean hasTimeConflicts(Integer studentId, FullCourse fullCourse){
        List<Integer> selectedCourseIds = courseStudentMapper.getCourseIdsByStudentId(studentId);

        if(selectedCourseIds == null){
            return false;
        }

        List<FullCourse> courseList = courseMapper.getCoursesByIds(selectedCourseIds);
        return CheckTimeConflictsUtils.checkCoursesAndCourse(courseList, fullCourse);
    }

    /**
     * 检查课程容量是否已满
     * @param courseId
     * @param fullCourse
     * @throws SelectCourseException
     */
    private void checkStorage(Integer courseId, FullCourse fullCourse) throws SelectCourseException {
        Integer studentsNum = courseMapper.getStudentNumById(courseId);
        if (studentsNum >= fullCourse.getCourseStorage()) {
            throw new SelectCourseException("课程容量已满", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 向关联表中插入记录
     * @param courseId
     * @param studentId
     */
    @Transactional
    public void insertSelectedCourse(Integer courseId, Integer studentId) {
        courseStudentMapper.selectCourse(courseId, studentId);
        courseMapper.incrementStudentNum(courseId);
    }
}
