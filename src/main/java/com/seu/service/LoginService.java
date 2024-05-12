package com.seu.service;

public interface LoginService {
    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     */
    boolean checkCredentials(String username, String password);
}
