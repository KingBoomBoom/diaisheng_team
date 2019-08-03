package com.qdu.diaisheng.service.impl;

import com.qdu.diaisheng.dao.UserDao;
import com.qdu.diaisheng.entity.User;
import com.qdu.diaisheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String userName, String password) {
       return  userDao.findByUserNameAndPassword(userName,password);
    }

    @Override
    public boolean register(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User findByUserName(String name) {
        return userDao.findByUserName(name);
    }
}
