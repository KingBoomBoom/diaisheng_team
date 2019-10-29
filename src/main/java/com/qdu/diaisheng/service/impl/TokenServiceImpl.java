package com.qdu.diaisheng.service.impl;

import com.qdu.diaisheng.dao.TokenDao;
import com.qdu.diaisheng.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenDao tokenDao;

    @Override
    public boolean checkAndSetToken(String token) {
         if(tokenDao.checkToken(token)&&(tokenDao.getTokenStatus(token)==0)){//如果token存在并且token没有被使用
             tokenDao.changeStatus();//更改token为已使用
             return  true;
         }else {
             return false;
         }
    }
}
