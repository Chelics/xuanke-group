package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Integer id;//主键
    private String courseName;//课程名
    private Short type;//类别
    private String courseNumber;//课程编号
    private Integer roomId;//对应教室主键
    private Short courseHour;//课时
    private Short courseStorage;//课程容量
    private Short startWeek;
    private Short endWeek;
    private Short time1;
    private Short time2;
    private Short time3;
    private String faculty;//学院
    private Short credit;//学分
    private Integer studentNum;//已选人数
}
