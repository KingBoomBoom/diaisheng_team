package com.qdu.diaisheng.service.impl;

import com.qdu.diaisheng.dao.PhotoDao;
import com.qdu.diaisheng.entity.Photo;
import com.qdu.diaisheng.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
