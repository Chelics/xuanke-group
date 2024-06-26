package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingCourse {
    private Integer id;//主键
    private String courseName;//课程名
    private Short type;//类别
    private String courseNumber;//课程编号
    private Short courseHour;//课时
    private Short courseStorage;//课程容量
    private String faculty;//学院
    private Short credit;//学分
    private Short courseStatus;//审核状态,说明: 1 待审核,2 已通过,3 已驳回
    private String teacherIds;//用,分隔
    private String classIds;//用,分隔
    private LocalDateTime commitTime;//提交时间
}
