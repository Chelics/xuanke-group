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
        checkInput(fullCourse, studentId);

        Lock lock = lockMap.computeIfAbsent(courseId, k -> new ReentrantLock());
        lock.lock();
        //检查课程容量是否已满
        checkStorage(courseId, fullCourse);

        //向关联表中插入记录
        insertSelectedCourse(courseId, studentId);

        lock.unlock();

        return true;
    }

    /**
     * 确认学生和课程都存在, 且待选课程与已选课程没有时间冲突
     * @param fullCourse
     * @param studentId
     * @return
     */
    private void checkInput(FullCourse fullCourse, Integer studentId) throws SelectCourseException, EntityNotFoundException {

        //判断是否处在可以选课的阶段
        checkStageService.checkStage();

        if (fullCourse == null) {
            throw new SelectCourseException("课程不存在", HttpStatus.BAD_REQUEST);
        }

        Student student = studentMapper.getStudentById(studentId);
        if(student == null){
            throw new SelectCourseException("学生不存在: " + studentId, HttpStatus.BAD_REQUEST);
        }

        if (hasTimeConflicts(studentId, fullCourse)){
            log.info("检查到课程时间冲突: {}", fullCourse.getCourseName());
            throw new SelectCourseException("课程已选或与其它已选课程存在时间冲突", HttpStatus.BAD_REQUEST);
        }

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
        Integer studentsCountForCourse = courseStudentMapper.getStudentCountForCourse(courseId);
        int count = (studentsCountForCourse == null) ? 0 : studentsCountForCourse;
        if (count >= fullCourse.getCourseStorage()) {
            throw new SelectCourseException("课程已满: " + courseId, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 向关联表中插入记录
     * @param courseId
     * @param studentId
     * @throws SelectCourseException
     */
    private void insertSelectedCourse(Integer courseId, Integer studentId) throws SelectCourseException {
        if(courseStudentMapper.selectCourse(courseId, studentId) <= 0){
            throw new SelectCourseException("数据库操作失败: 学生id: " + studentId + "课程id: " + courseId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
