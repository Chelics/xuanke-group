package com.seu.service.impl;

import com.seu.config.SecurityConfig;
import com.seu.mapper.StaffMapper;
import com.seu.mapper.StudentMapper;
import com.seu.mapper.TeacherMapper;
import com.seu.pojo.Users.User;
import com.seu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    SecurityConfig securityConfig;

    @Override
    public User checkCredentials(String username, String password) {

        User user = null;

        if (username.charAt(0) == '0') {
            user = studentMapper.getStudentByUsername(username);
        }else if(username.charAt(0) == '1'){
            user = teacherMapper.getByUsername(username);
        }else if(username.charAt(0) == '2'){
            user = staffMapper.getByUsername(username);
        }

        if(user != null && securityConfig.matchPassword(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}
