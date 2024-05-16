package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullCheckingCourse extends CheckingCourse{
    private List<String> teachers;//教师名
    private List<String> classes;//班级名
}
