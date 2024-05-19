package com.seu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 登录信息类
 * 只包含username和password, 用于验证登录信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginData {
    private String username;
    private String password;
    //验证码等, 待补充
}
