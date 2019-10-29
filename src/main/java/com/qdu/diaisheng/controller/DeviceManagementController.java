package com.qdu.diaisheng.controller;


import com.alibaba.fastjson.JSONObject;
import com.qdu.diaisheng.dao.DeviceDao;
import com.qdu.diaisheng.entity.Device;
import com.qdu.diaisheng.entity.User;
import com.qdu.diaisheng.service.DeviceService;
import com.qdu.diaisheng.util.HttpServletUtil;

import com.qdu.diaisheng.util.ImageUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/deviceadmin")
public class DeviceManagementController {

    //"diaisheng/deviceadmin/getdevice"

    @Autowired
    private DeviceService deviceService;
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


    /**
     * @author wangxi
     * @Description 根据user_id获取设备
     * 通过modelMap返回数据
     * @date  2019/8/4
     * @return Map
     * @throws
     * @since
     */
    @RequestMapping(value = "/getdevice",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>getDevice(HttpServletRequest request){

        User user=(User) request.getSession().getAttribute("loginUser");
        Map<String,Object>modelMap=new HashMap<>();
        if(user==null){
            modelMap.put("success",false);
            modelMap.put("redirect","/diaisheng/admin/login");
            return modelMap;
        }else{
            List<Device> deviceList=deviceService.getDeviceList(user);
            if(deviceList!=null&&deviceList.size()>0){
                modelMap.put("success",true);
                modelMap.put("device",deviceList);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","没有查到设备");
            }

        }


        return modelMap;
    }


    /**
     * @author wangxi
     * @Description 获取基站的位置信息
     * @date  2019/8/28
     * @return 返回JSON数据
     * errcode
     * 0: 成功
     * 10000: 参数错误
     * 10001: 无查询结果
     * @throws
     * @since
     */
    @RequestMapping(value = "/getlocation",method = RequestMethod.GET)
    @ResponseBody
    public String getDeviceLoaction(){

        int mcc=460,mnc=1,lac=4301,ci=20986;
        String url="http://api.cellocation.com:81/cell/";
        CloseableHttpClient client=null;
        HttpGet httpGet=null;
        HttpResponse response=null;
        String res=null;
        Map<String,Object>map=new HashMap<>();
        try{
            String charset="UTF-8";
            client= HttpClientBuilder.create().build();
            httpGet=new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).build();
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Content-Type","application/x-www-form-urlencoded");
            httpGet.addHeader("charset",charset);
            httpGet.setURI(URI.create(url + "?mcc="+mcc+"&mnc="+mnc+"&lac="+lac+"&ci="+ci+"&output=json&coord=gcj02"));
            response=client.execute(httpGet);
             res= EntityUtils.toString(response.getEntity());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            httpGet.releaseConnection();
            try{
                if(client!=null){
                    client.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return res;
    }




}
