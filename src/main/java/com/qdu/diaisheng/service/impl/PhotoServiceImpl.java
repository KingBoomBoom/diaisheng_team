package com.qdu.diaisheng.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qdu.diaisheng.dao.PhotoDao;
import com.qdu.diaisheng.entity.Photo;
import com.qdu.diaisheng.service.PhotoService;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoDao photoDao;

    @Override
    public int addPhoto(Photo photo) {
        if(photo==null) {
            return -1;
        }else {
            int row=photoDao.addPhoto(photo);
            if(row<=0)
                return -1;
        }
        return 1;
    }

    @Override
    public Photo getNewPhoto(String cameraId) {
        if(cameraId==null)
            return null;
        else {
            return photoDao.getNewPhotoByCamera(cameraId);
        }
    }




}
