package com.qdu.diaisheng.service;

import com.qdu.diaisheng.entity.Photo;

public interface PhotoService {
    public int addPhoto(Photo photo);
    public Photo getNewPhoto(String cameraId);

}
