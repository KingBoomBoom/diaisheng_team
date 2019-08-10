package com.qdu.diaisheng.controller;

import com.qdu.diaisheng.util.Md5;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
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
}
