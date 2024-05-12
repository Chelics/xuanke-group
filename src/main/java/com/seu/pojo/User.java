package com.seu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
abstract class User {
    private String username;
    private String password;
    private String name;
}

