package com.seu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//没有密码的teacher类
public class Teacher{
    private Integer id;
    private String username;
    private String teacherName;
}
