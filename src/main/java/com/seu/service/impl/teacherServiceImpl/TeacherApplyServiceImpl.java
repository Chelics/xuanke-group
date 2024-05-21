package com.seu.service.impl.teacherServiceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seu.mapper.*;
import com.seu.pojo.CheckingCourse;
import com.seu.pojo.FullCheckingCourse;
import com.seu.pojo.PageBean;
import com.seu.service.teacherService.TeacherApplyService;
import com.seu.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherApplyServiceImpl implements TeacherApplyService {

    @Autowired
    private CheckingMapper checkingMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private ClassMapper classMapper;


    @Override
    public void applyCourse(CheckingCourse applyingCourse){
        //applyingCourse.setCourseStatus((short) 1);用不到，数据库里默认为1
        applyingCourse.setCommitTime(LocalDateTime.now());
        checkingMapper.add(applyingCourse);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public PageBean page(Integer id,Integer page, Integer pageSize,Short status){
        //此id为教师id
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询
        List<FullCheckingCourse> fullCheckingCours= checkingMapper.page(id,status);
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
}
