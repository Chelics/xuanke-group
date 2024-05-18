package com.seu.dto.response;

import com.seu.pojo.FullCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTable {
    List<FullCourse> courseList;
}
