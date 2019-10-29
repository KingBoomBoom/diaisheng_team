package com.qdu.diaisheng.dao;

public interface TokenDao {
    public boolean checkToken(String token);//验证是否存在该token
    public void changeStatus();//更改token的状态为1
    public Integer getTokenStatus(String token);//获取token的状态
}
