package com.qdu.diaisheng.service;

import com.qdu.diaisheng.entity.Photo;

import java.util.List;

public interface PhotoService {
    public int addPhoto(Photo photo);
    public Photo getNewPhoto(String cameraId);
    public List<Photo> getHistoryPhotos(String stime,String etime);
}
