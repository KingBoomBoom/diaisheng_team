package com.qdu.diaisheng.service.impl;

import com.qdu.diaisheng.dao.PhotoDao;
import com.qdu.diaisheng.entity.Camera;
import com.qdu.diaisheng.entity.Photo;
import com.qdu.diaisheng.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Camera getCameraBydeviceId(String deviceId) {
        if (deviceId==null||deviceId.equals("")){
            return null;
        }
        return photoDao.getCameraIdBydeviceId(deviceId);
    }

    /**
     * 查询历史图片数据
     * @param stime
     * @param etime
     * @return list
     */
    @Override
    public List<Photo> getHistoryPhotos(String stime, String etime,String cameraId) {
        if (stime==null||etime==null||cameraId==null)
            return null;
        else{
            return photoDao.getHistoryPhotos(stime,etime,cameraId);
        }
    }


}
