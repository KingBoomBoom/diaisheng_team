package com.qdu.diaisheng.task;

import com.alibaba.fastjson.JSONObject;
import com.qdu.diaisheng.entity.Photo;
import com.qdu.diaisheng.service.PhotoService;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Component
public class PhotoTimerTask {

    @Autowired
    private PhotoService photoService;
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * @Author wangxi
     * @Description 定时执行任务，每半小时从接口获取图片的Base64编码并存入数据库中
     * 有错误，暂时注释
     * @Date 2019/7/21 23:35
     * @Param [request, response]
     * @return
     **/

   // @Scheduled(cron = "0 */1 * * * ?")
    /*
    public void timerAddPhoto(){
        String url="http://39.108.213.89:10100/GetSnapshotPic";
        Map<String,Object> map=new HashMap<>();
        CloseableHttpClient client=null;
        HttpGet httpGet=null;
        HttpResponse response=null;
        try{
            String charset="UTF-8";
            client= HttpClientBuilder.create().build();
            httpGet=new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).build();
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Content-Type","application/x-www-form-urlencoded");
            httpGet.addHeader("charset",charset);
            httpGet.setURI(URI.create(url + "?deviceid=c844150007b9&serverip=39.108.213.89"));
            response=client.execute(httpGet);
            String res= EntityUtils.toString(response.getEntity());
            JSONObject jsonObject=JSONObject.parseObject(res);
            String Base64=jsonObject.getString("picBase64");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date current=new Date();
            String formatCurrent=sdf.format(current);
            Photo p=new Photo();
            p.setCameraId("c844150007b9");
            p.setCreateTime(formatCurrent);
            p.setContent(Base64);
            if(photoService.addPhoto(p)>0){
                logger.info("添加成功");
            }
        }catch (Exception e){
            map.put("msg","java获取设备实时抓拍图片接口异常");
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
    }
*/

}
