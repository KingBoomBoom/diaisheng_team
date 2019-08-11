package com.qdu.diaisheng.controller;

import com.qdu.diaisheng.util.ImageUtil;
import com.qdu.diaisheng.util.Md5;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.xml.ws.Response;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

/**
 * @ClassName PhotoDemoController
 * @Author Administrator
 * @description 定时拍照相关接口调用demo，仅供参考用。
 * @Date 2019/8/9 14:53
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/photos")
public class PhotoDemoController {
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * @Author changliang
     * @Description 登录拍照设备，获取账户控制令牌（access_token），有效期24小时
     * @Date 2019/8/9 14:55
     * PostMethod
     **/
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map<String,Object> login() {
        String url = "http://user.hddvs.net:8080/apiv2/LoginAction.php";
        Map<String, Object> map = new HashMap<>();
        HttpClient client = null;
        PostMethod method = null;
        try {
            String charset = "UTF-8";
            client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);// 设置连接时间
            method = new PostMethod(url);//post请求
            method.getParams().setContentCharset(charset);//设置传入参数编码为UTF-8
            method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");//设置php头文件设置，不然会出现非法参数异常
            method.setRequestHeader("charset","UTF-8");
            method.setParameter("username", "my51c");
            method.setParameter("password", Md5.md5("my51c"));
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();//随机生成uuid
            method.setParameter("state", uuid);
            int code = client.executeMethod(method);
            map.put("msg","java访问php登陆接口成功");
            map.put("code", code);//200表示成功，其余表示失败
            map.put("data",method.getResponseBodyAsString());//获取封装的json数据，以便下一步操作
            System.out.println(map.get("data"));
            logger.info("java访问php登陆接口成功");
            return map;
        } catch (Exception e) {
            map.put("msg","java访问php登陆接口异常");
            logger.error("java访问php登陆接口异常", e);
        } finally {
            method.releaseConnection();
            client=null;
        }
        return map;
    }
    /**
     * @Author changliang
     * @Description 登录拍照设备，获取账户控制令牌（access_token），有效期24小时
     * @Date 2019/8/9 14:55
     * HttpPost
     **/
    @RequestMapping(value = "/login2")
    @ResponseBody
    public Map<String,Object> loginDemo(){
        String url = "http://user.hddvs.net:8080/apiv2/LoginAction.php";
        Map<String, Object> map = new HashMap<>();
        CloseableHttpClient client = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        try {
            String charset = "UTF-8";
            client = HttpClientBuilder.create().build();
            httpPost=new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);//设置post状态参数
            httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.addHeader("charset","UTF-8");
            List<BasicNameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("username", "my51c"));
            list.add(new BasicNameValuePair("password", Md5.md5("my51c")));
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();//随机生成uuid
            System.out.println(uuid);
            list.add(new BasicNameValuePair("state", uuid));
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list,"UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            //response响应
            response = client.execute(httpPost);
            String res = EntityUtils.toString(response.getEntity());
            map.put("data",res);//获取封装的json数据，以便下一步操作
            System.out.println(map.get("data"));
            logger.info("java访问php登陆接口成功");
            return map;
        } catch (Exception e) {
            map.put("msg","java访问php登陆接口异常");
            logger.error("java访问php登陆接口异常", e);
        } finally {
            httpPost.releaseConnection();
            try {
                if (client!=null)
                     client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * @Author wangxi
     * @Description  获取组设备列表
     * @Date 2019/8/11 12:55
     * HttpPost
     **/

    @RequestMapping(value = "/getDeviceList")
    @ResponseBody
    public Map<String,Object>getDeviceList(String accessToken,String token){

        String url="http://user.hddvs.net:8080/apiv2/GetGroupDeviceAction.php";
        Map<String,Object>map=new HashMap<>();
        CloseableHttpClient client=null;
        HttpPost post=null;
        HttpResponse response=null;
        try{
            String charset="UTF-8";
            client=HttpClientBuilder.create().build();
            post=new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            post.setConfig(requestConfig);
            post.addHeader("Content-Type","application/x-www-form-urlencoded");
            post.addHeader("charset",charset);
            List<BasicNameValuePair>params=new ArrayList<>();
            params.add(new BasicNameValuePair("username","my51c"));
            params.add(new BasicNameValuePair("access_token",accessToken));
            params.add(new BasicNameValuePair("state",token));
            UrlEncodedFormEntity urlEncodedFormEntity=new UrlEncodedFormEntity(params,charset);
            post.setEntity(urlEncodedFormEntity);
            response=client.execute(post);
            String res=EntityUtils.toString(response.getEntity());
            map.put("data",res);
        }catch (Exception e){
            map.put("msg","java访问php登陆接口异常");
            logger.error("java访问php登陆接口异常", e);
            e.printStackTrace();
        }finally {
            post.releaseConnection();
            try{
            if(client!=null){
                client.close();
            }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return map;

    }


    /**
     * @Author wangxi
     * @Description  保存设备信息
     * @Date 2019/8/11 16:55
     * HttpPost
     **/
    @RequestMapping(value = "/saveDevice")
    @ResponseBody
    public Map<String,Object> saveDevice(){
        String url="http://39.108.213.89:10100/SaveDevInfo";
        CloseableHttpClient client=null;
        Map<String,Object>map=new HashMap<>();
        HttpPost post=null;
        HttpResponse response=null;
        String charset="UTF-8";
        try{
            post=new HttpPost(url);
            RequestConfig requestConfig=RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
            post.setConfig(requestConfig);
            post.addHeader("Content-Type","application/x-www-form-urlencoded");
            post.addHeader("charset",charset);
            JSONObject postData=new JSONObject();
            postData.put("deviceid","");
            postData.put("serverip","");
            postData.put("devicename","");
            postData.put("Snaptime1","");
            postData.put("Snaptime2","");
            postData.put("SnapInterval","");
            postData.put("FtpAddr","");
            postData.put("FtpUserName","");
            postData.put("FtpPassword","");
            postData.put("FtpPort","");
            post.setEntity(new StringEntity(postData.toString(), HTTP.UTF_8));
            client.execute(post);
            map.put("success",true);
        }catch (Exception e){
            map.put("msg","java访问php登陆接口异常");
            map.put("success",false);
            logger.error("java访问php登陆接口异常", e);
            e.printStackTrace();
        }finally {
            post.releaseConnection();
            try{
                if(client!=null){
                    client.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * @Author wangxi
     * @Description  获取设备实时抓拍图片
     * @Date 2019/8/11 16:55
     * HttpPost
     **/
    @RequestMapping(value = "/getSnapshotPhoto")
    @ResponseBody
    public Map<String,Object>GetSnapshotPic(){

        String url="http://39.108.213.89:10100/GetSnapshotPic";
        Map<String,Object>map=new HashMap<>();
        CloseableHttpClient client=null;
        HttpGet httpGet=null;
        HttpResponse response=null;
        try{
            String charset="UTF-8";
            client=HttpClientBuilder.create().build();
            httpGet=new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).build();
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Content-Type","application/x-www-form-urlencoded");
            httpGet.addHeader("charset",charset);
            httpGet.setURI(URI.create(url + "?deviceid=c84429000055&serverip=39.108.213.89"));
            response=client.execute(httpGet);
            String res=EntityUtils.toString(response.getEntity());
            com.alibaba.fastjson.JSONObject json_test = com.alibaba.fastjson.JSONObject.parseObject(res);
            ImageUtil.Base64ToImage(json_test.get("picBase64").toString());
            map.put("data",res);
        }catch (Exception e){
            map.put("msg","java访问php登陆接口异常");
            logger.error("java访问php登陆接口异常", e);
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

        return map;

    }






}
