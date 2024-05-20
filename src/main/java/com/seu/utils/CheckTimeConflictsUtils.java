package com.seu.utils;

import com.seu.pojo.Course;
import com.seu.pojo.FullCourse;

import java.util.List;

/**
 * CheckTimeConflictsUtils类
 * 用于检查时间冲突, 冲突时返回true
 */
public class CheckTimeConflictsUtils {
    private static final int TIME_SLOT = 8;
    private static final int WEEKDAY_SLOT = 7;

    /**
     * 检查两个课程是否存在冲突
     * @param course1
     * @param course2
     * @return true表示冲突
     */
    public static boolean checkCourseAndCourse(Course course1, Course course2) {

        if(course1.getEndWeek() < course2.getStartWeek() || course2.getEndWeek() < course1.getStartWeek()){
            return false;
        }

        return checkTimeAndTime(course1.getTime1(), course2.getTime1()) ||
                checkTimeAndTime(course1.getTime1(), course2.getTime2()) ||
                checkTimeAndTime(course1.getTime1(), course2.getTime3()) ||
                checkTimeAndTime(course1.getTime2(), course2.getTime1()) ||
                checkTimeAndTime(course1.getTime2(), course2.getTime2()) ||
                checkTimeAndTime(course1.getTime2(), course2.getTime3()) ||
                checkTimeAndTime(course1.getTime3(), course2.getTime1()) ||
                checkTimeAndTime(course1.getTime3(), course2.getTime2()) ||
                checkTimeAndTime(course1.getTime3(), course2.getTime3());
    }

    /**
     * 检查两个时间是否存在冲突
     * @param time1
     * @param time2
     * @return true表示冲突
     */
    public static boolean checkTimeAndTime(Short time1, Short time2) {
        if (time1 == null || time2 == null) {
            return false;
        }

        int jie1 = time1 % TIME_SLOT;
        int jie2 = time2 % TIME_SLOT;
        int weekDay1 = time1 / TIME_SLOT;
        int weekDay2 = time2 / TIME_SLOT;
        int week1 = weekDay1 / WEEKDAY_SLOT;
        int week2 = weekDay2 / WEEKDAY_SLOT;
        int day1 = weekDay1 % WEEKDAY_SLOT;
        int day2 = weekDay2 % WEEKDAY_SLOT;

        return (jie1 == jie2) && (day1 == day2) && ((week1 + week2 != 1) || (week1 * week2 != 0));
    }

    /**
     * 检查1个时间和1个课程是否冲突
     * @param fullCourse
     * @param time
     * @return true表示冲突
     */
    public static boolean checkCourseAndTime(FullCourse fullCourse, Short time){
        return checkTimeAndTime(fullCourse.getTime1(), time) ||
                checkTimeAndTime(fullCourse.getTime2(), time) ||
                checkTimeAndTime(fullCourse.getTime3(), time);
    }

    /**
     * 检查1个时间和一组课程是否冲突
     * @param courseList
     * @param time
     * @return true表示冲突
     */
    public static boolean checkCoursesAndTime(List<FullCourse> courseList, Short time){
        for(FullCourse course : courseList){
            if(checkCourseAndTime(course, time)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查一组时间和一组课程是否冲突
     * @param courseList
     * @param timeList
     * @return true表示冲突
     */
    public static boolean checkCoursesAndTimes(List<FullCourse> courseList, short[] timeList){
        for(Short time : timeList){
            if(checkCoursesAndTime(courseList, time)){
                return true;
            }
        }

        return false;
    }

    /**
     * 检查一组课程和一门课程是否冲突
     * @param courseList
     * @param fullCourse
     * @return true表示冲突
     */
    public static boolean checkCoursesAndCourse(List<FullCourse> courseList, FullCourse fullCourse){
        for(FullCourse course : courseList){
            if(checkCourseAndCourse(course, fullCourse)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查一组课程和一组课程是否冲突
     * @param courseList1
     * @param courseList2
     * @return true表示冲突
     */
    public static boolean checkCoursesAndCourses(List<FullCourse> courseList1, List<FullCourse> courseList2){
        for(FullCourse course1 : courseList1){
            if(checkCoursesAndCourse(courseList2, course1)){
                return true;
            }
        }
        return false;
    }
}

