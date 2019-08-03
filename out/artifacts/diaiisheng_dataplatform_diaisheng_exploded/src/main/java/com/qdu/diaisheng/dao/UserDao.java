package com.qdu.diaisheng.dao;

import com.qdu.diaisheng.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    User  findByUserNameAndPassword(@Param("userName")String userName,
                                    @Param("password")String password);
    boolean addUser(User user);

    User findByUserName(String userName);

}
