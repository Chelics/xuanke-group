package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
//@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class MyClass {
    private Integer id;//主键
    private String className;//班级名称
}
