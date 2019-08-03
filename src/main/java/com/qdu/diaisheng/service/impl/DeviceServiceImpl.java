package com.qdu.diaisheng.service.impl;

import com.qdu.diaisheng.dao.DeviceDao;
import com.qdu.diaisheng.entity.Device;
import com.qdu.diaisheng.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    DeviceDao deviceDao;

    @Override
    public List<Device> getDeviceList(int userId) {
        return deviceDao.queryDevice(userId);
    }
}
