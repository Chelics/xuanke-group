package com.seu.utils;

import com.seu.pojo.Course;

/**
 * CheckTimeConflictsUtils类
 * 用于检查时间冲突, 冲突时返回true
 */
public class CheckTimeConflictsUtils {
    private static final int TIME_SLOT = 8;
    private static final int WEEKDAY_SLOT = 7;

    public static boolean checkTimeConflicts(Course course1, Course course2) {

        if(course1.getEndWeek() < course2.getStartWeek() || course2.getEndWeek() < course1.getStartWeek()){
            return false;
        }

        return checkTimeConflict(course1.getTime1(), course2.getTime1()) ||
                checkTimeConflict(course1.getTime1(), course2.getTime2()) ||
                checkTimeConflict(course1.getTime1(), course2.getTime3()) ||
                checkTimeConflict(course1.getTime2(), course2.getTime1()) ||
                checkTimeConflict(course1.getTime2(), course2.getTime2()) ||
                checkTimeConflict(course1.getTime2(), course2.getTime3()) ||
                checkTimeConflict(course1.getTime3(), course2.getTime1()) ||
                checkTimeConflict(course1.getTime3(), course2.getTime2()) ||
                checkTimeConflict(course1.getTime3(), course2.getTime3());
    }

    private static boolean checkTimeConflict(Short time1, Short time2) {
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
}

