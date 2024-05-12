package com.seu.service.impl;

import com.seu.service.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public boolean checkCredentials(String username, String password) {
        return true;
    }
}
