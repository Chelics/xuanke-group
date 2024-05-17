package com.seu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seu.mapper.*;
import com.seu.pojo.ApplyingCourse;
import com.seu.pojo.FullCheckingCourse;
import com.seu.pojo.PageBean;
import com.seu.pojo.Stage;
import com.seu.service.TeacherCourseService;
import com.seu.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {

    @Autowired
    private ApplyingMapper applyingMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private ClassMapper classMapper;


    @Override
    public void applyCourse(ApplyingCourse applyingCourse){

    }


    @Override
    public PageBean pageNotReviewed(Integer page, Integer pageSize){
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询

        List<ApplyingCourse> applyingCours= applyingMapper.listNotReviewed();
        //3.补充完整
        for (int i = 0; i < applyingCours.size(); i++) {
            ApplyingCourse applyingCourse = applyingCours.get(i);//对于每一个course
            //使用IdUtils类 解码','分隔的teacher和class
            List<Integer> teacherIDs = IdUtils.stringToList(applyingCourse.getTeacherIds());
            List<Integer> classIDs = IdUtils.stringToList(applyingCourse.getClassIds());

            List<String> teachers=teacherMapper.getNamesByIds(teacherIDs);
            applyingCourse.setTeachers(teachers);
            List<String> classes=classMapper.getNamesByIds(classIDs);
            applyingCourse.setClasses(classes);
        }
        Page<ApplyingCourse> p=(Page<ApplyingCourse>)applyingCours;
        //4. 封装pageBean 对象
        PageBean pageBean=new PageBean(p.getResult(),p.getTotal());

        return pageBean;
    }

    @Override
    public PageBean pagePassed(Integer page, Integer pageSize){
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询

        List<ApplyingCourse> applyingCours= applyingMapper.listPassed();
        //3.补充完整
        for (int i = 0; i < applyingCours.size(); i++) {
            ApplyingCourse applyingCourse = applyingCours.get(i);//对于每一个course
            //使用IdUtils类 解码','分隔的teacher和class
            List<Integer> teacherIDs = IdUtils.stringToList(applyingCourse.getTeacherIds());
            List<Integer> classIDs = IdUtils.stringToList(applyingCourse.getClassIds());

            List<String> teachers=teacherMapper.getNamesByIds(teacherIDs);
            applyingCourse.setTeachers(teachers);
            List<String> classes=classMapper.getNamesByIds(classIDs);
            applyingCourse.setClasses(classes);
        }
        Page<ApplyingCourse> p=(Page<ApplyingCourse>)applyingCours;
        //4. 封装pageBean 对象
        PageBean pageBean=new PageBean(p.getResult(),p.getTotal());

        return pageBean;
    }

    @Override
    public PageBean pageRejected(Integer page, Integer pageSize){
        //1. 设置分页参数
        PageHelper.startPage(page,pageSize);
        //2. 执行查询

        List<ApplyingCourse> applyingCours= applyingMapper.listRejected();
        //3.补充完整
        for (int i = 0; i < applyingCours.size(); i++) {
            ApplyingCourse applyingCourse = applyingCours.get(i);//对于每一个course
            //使用IdUtils类 解码','分隔的teacher和class
            List<Integer> teacherIDs = IdUtils.stringToList(applyingCourse.getTeacherIds());
            List<Integer> classIDs = IdUtils.stringToList(applyingCourse.getClassIds());

            List<String> teachers=teacherMapper.getNamesByIds(teacherIDs);
            applyingCourse.setTeachers(teachers);
            List<String> classes=classMapper.getNamesByIds(classIDs);
            applyingCourse.setClasses(classes);
        }
        Page<ApplyingCourse> p=(Page<ApplyingCourse>)applyingCours;
        //4. 封装pageBean 对象
        PageBean pageBean=new PageBean(p.getResult(),p.getTotal());

        return pageBean;
    }
}
