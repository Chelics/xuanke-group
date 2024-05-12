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
abstract class User {
    private String username;
    private String password;
    private String name;
}

