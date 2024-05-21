package com.seu.service.impl.staffServiceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seu.exception.AllocateFailureException;
import com.seu.mapper.CheckingMapper;
import com.seu.mapper.ClassMapper;
import com.seu.mapper.TeacherMapper;
import com.seu.pojo.FullCheckingCourse;
import com.seu.pojo.PageBean;
import com.seu.service.impl.CourseSchedulerImpl;
import com.seu.service.staffService.StaffCheckingService;
import com.seu.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffCheckingServiceImpl implements StaffCheckingService {
    @Autowired
    private CheckingMapper checkingMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private CourseSchedulerImpl scheduler;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public PageBean page(Integer page, Integer pageSize, String courseName) {
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询
        List<FullCheckingCourse> fullCheckingCours= checkingMapper.list(courseName);
        //3.补充完整审核项
        for (int i = 0; i < fullCheckingCours.size(); i++) {
            FullCheckingCourse fullCheckingCourse = fullCheckingCours.get(i);//对于每一个course
            //使用IdUtils类 解码','分隔的teacher和class
            List<Integer> teacherIDs = IdUtils.stringToList(fullCheckingCourse.getTeacherIds());
            List<Integer> classIDs = IdUtils.stringToList(fullCheckingCourse.getClassIds());
            //补充完整信息
            List<String> teachers=teacherMapper.getNamesByIds(teacherIDs);
            fullCheckingCourse.setTeachers(teachers);
            List<String> classes=classMapper.getNamesByIds(classIDs);
            fullCheckingCourse.setClasses(classes);
        }
        Page<FullCheckingCourse> p=(Page<FullCheckingCourse>)fullCheckingCours;
        //3. 封装pageBean 对象
        PageBean pageBean=new PageBean(p.getResult(),p.getTotal());

        return pageBean;
    }

    @Override
    public void reject(List<Integer> ids) {
        checkingMapper.reject(ids);
    }

    @Transactional
    @Override
    public void pass(List<Integer> ids) throws AllocateFailureException {
        checkingMapper.pass(ids);

        //自动分配时间和教室
        scheduler.allocateMultipleCourses(ids);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public FullCheckingCourse getById(Integer id) {
        FullCheckingCourse basic=checkingMapper.getById(id);
        //使用IdUtils类 解码','分隔的teacher和class
        List<Integer> teacherIDs = IdUtils.stringToList(basic.getTeacherIds());
        List<Integer> classIDs = IdUtils.stringToList(basic.getClassIds());
        //补充完整信息
        List<String> teachers=teacherMapper.getNamesByIds(teacherIDs);
        basic.setTeachers(teachers);
        List<String> classes=classMapper.getNamesByIds(classIDs);
        basic.setClasses(classes);
        return basic;
    }
}
