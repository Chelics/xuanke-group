package com.seu.pojo.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;//应是int
    private String username;
    private String password;
    private String name;
}

