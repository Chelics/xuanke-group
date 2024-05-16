package com.seu.service;

import com.seu.pojo.Users.User;

public interface LoginService {
    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     */
    User checkCredentials(String username, String password);
}
