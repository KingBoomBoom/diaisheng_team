package com.qdu.diaisheng.dao;

import com.qdu.diaisheng.entity.Device;
import java.util.List;

public interface DeviceDao {
    int insertDevice(Device device);
    List<Device> queryDevice(int userId);

}
