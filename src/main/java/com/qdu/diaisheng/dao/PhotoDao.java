package com.qdu.diaisheng.dao;

import com.qdu.diaisheng.entity.Photo;
import org.apache.ibatis.annotations.Param;

public interface PhotoDao {
    public int addPhoto(Photo photo);
    public Photo getNewPhotoByCamera(@Param("cameraId")String cameraId);
}
