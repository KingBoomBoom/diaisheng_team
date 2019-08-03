package com.qdu.diaisheng.service;

import com.qdu.diaisheng.entity.User;

public interface UserService {
    User login(String user_name,String password);
    boolean register(User user);

    User findByUserName(String name);
}
