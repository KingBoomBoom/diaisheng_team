package com.qdu.diaisheng.controller;


import com.qdu.diaisheng.dao.DeviceDao;
import com.qdu.diaisheng.entity.Device;
import com.qdu.diaisheng.entity.User;
import com.qdu.diaisheng.service.DeviceService;
import com.qdu.diaisheng.util.HttpServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/deviceadmin")
public class DeviceManagementController {


    @Autowired
    private DeviceDao deviceDao;

    @RequestMapping(value = "/adddevice",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addDevice(HttpServletRequest request){
        User user=(User) request.getSession().getAttribute("user");
        Map<String,Object>modelMap=new HashMap<>();
        if(user==null){
            modelMap.put("success",false);
            modelMap.put("redirect",true);
            return modelMap;
        }else{
            Device device=new Device();
            String deviceId=HttpServletUtil.getString(request,"deviceId");
            int userId=user.getUserId();
            String deviceName=HttpServletUtil.getString(request,"deviceName");
            String createTime=new Date().toString();
            if(device!=null&&deviceName!=null&&userId>0){
                device.setDeviceId(deviceId);
                device.setDeviceName(deviceName);
                device.setCreateTime(createTime);
                device.setUserId(userId);
                int effectedNum=deviceDao.insertDevice(device);
                if(effectedNum>0){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                }
               // device.set
            }

        }


        return modelMap;

    }



}
