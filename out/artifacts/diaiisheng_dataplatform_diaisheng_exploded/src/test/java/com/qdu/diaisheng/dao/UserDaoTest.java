package com.qdu.diaisheng.dao;

import com.qdu.diaisheng.BaseTest;
import com.qdu.diaisheng.entity.User;
import com.qdu.diaisheng.util.Md5;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    @Test
    @Ignore
    public void testAddUser(){
        User user=new User();
        user.setUserName("smiao");
        String password= Md5.md5("diaisheng001");
        user.setPassword(password);
        boolean flag=userDao.addUser(user);
        System.out.println(flag);

    }

    @Test
    public void testLogin(){
        String userName="smiao";
        String password=Md5.md5("diaisheng001");
        User user=userDao.findByUserNameAndPassword(userName,password);
        System.out.println(user);
    }


}
