package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
<<<<<<< HEAD:src/main/java/com/seu/pojo/ApplyingCourse.java
import lombok.EqualsAndHashCode;
=======
import lombok.NoArgsConstructor;
>>>>>>> deaf9a05ae932250df0c112ec1261c939249a434:src/main/java/com/seu/pojo/MyClass.java

@Data
//@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class MyClass {
    private Integer id;//主键
    private String className;//班级名称
}
