package com.seu.dto.response;

import com.seu.pojo.MyClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassList {
    private List<MyClass> classList;
}
