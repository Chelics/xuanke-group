package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullCourse extends Course{
    private List<String> teachers;
    private List<String> classes;
    private String roomName;
}
