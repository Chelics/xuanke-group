package com.seu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程请求信息类
 * 只包含课程id和学生id, 用于选课和退课
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSelection {
    private Integer courseId;
}
