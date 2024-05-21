package com.seu.service.impl.studentServiceImpl;

import com.seu.exception.SelectCourseFailureException;
import com.seu.mapper.CourseMapper;
import com.seu.mapper.CourseStudentMapper;
import com.seu.mapper.StageMapper;
import com.seu.mapper.StudentMapper;
import com.seu.pojo.Course;
import com.seu.pojo.FullCourse;
import com.seu.pojo.Stage;
import com.seu.pojo.Users.Student;
import com.seu.service.studentService.SelectCourseService;
import com.seu.utils.CheckTimeConflictsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class SelectCourseServiceImpl implements SelectCourseService {

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CourseStudentMapper courseStudentMapper;
    @Autowired
    StageMapper stageMapper;

    /**
     * 学生选课
     * @param courseId
     * @param studentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    //@Retryable(value = { SQLException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public synchronized boolean selectCourse(Integer courseId, Integer studentId) throws SelectCourseFailureException {

        FullCourse fullCourse = courseMapper.getCourseById(courseId);

        //输入检查 & 时间冲突检查
        checkInput(fullCourse, studentId);

        //课程未满
        Integer studentsCountForCourse = courseStudentMapper.getStudentCountForCourse(courseId);
        int count = (studentsCountForCourse == null) ? 0 : studentsCountForCourse;
        if (count >= fullCourse.getCourseStorage()) {
            throw new SelectCourseFailureException("课程已满");
        }

        if(courseStudentMapper.selectCourse(courseId, studentId) > 0){
            return true;
        }else{
            throw new SelectCourseFailureException("数据库操作失败");
        }
    }

    /**
     * 确认学生和课程都存在, 且待选课程与已选课程没有时间冲突
     * @param fullCourse
     * @param studentId
     * @return
     */
    private void checkInput(FullCourse fullCourse, Integer studentId) throws SelectCourseFailureException {

        //判断是否处在可以选课的阶段
        checkStage();

        if (fullCourse == null) {
            throw new SelectCourseFailureException("课程不存在");
        }

        Student student = studentMapper.getStudentById(studentId);
        if(student == null){
            throw new SelectCourseFailureException("学生不存在");
        }

        if (hasTimeConflicts(studentId, fullCourse)){
            throw new SelectCourseFailureException("课程已选或与其它已选课程存在时间冲突");
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

        for(Integer courId : selectedCourseIds){
            Course course = courseMapper.getCourseById(courId);
            if(CheckTimeConflictsUtils.checkCourseAndCourse(course, fullCourse)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否处在可以选课的阶段
     * @return
     */
    private void checkStage() throws SelectCourseFailureException {
        LocalDateTime now = LocalDateTime.now();
        Stage stage = stageMapper.getCurrStage(now);

        if(stage == null){
            throw new SelectCourseFailureException("未知的选课阶段");
        }

        String stageName = stage.getStageName();
        if(stageName.contains("-开放选课") || stageName.contains("退改补")){
            return;
        }

        if (stageName.contains("未开放")) {
            throw new SelectCourseFailureException("选课未开始");
        }else if(stageName.contains("结束")){
            throw new SelectCourseFailureException("选课已结束");
        }else{
            throw new SelectCourseFailureException("未知的选课阶段");
        }
    }
}
