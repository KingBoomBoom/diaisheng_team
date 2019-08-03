package com.qdu.diaisheng.dao;

import com.qdu.diaisheng.BaseTest;
import com.qdu.diaisheng.entity.Device;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Date;

public class DeviceDaoTest extends BaseTest {
  @Autowired
    private DeviceDao deviceDao;

  @Test
  @Ignore
  public void testInsertDevice(){
    Device device=new Device();
    device.setDeviceId("0000000001");
    device.setDeviceName("迪爱生");
   // device.setCreateTime(new Date());
    int effectedNum=deviceDao.insertDevice(device);
    System.out.println(effectedNum);
  }

  @Test
  public void testQueryDevice(){
    List<Device>deviceList=deviceDao.queryDevice();
    for(Device device:deviceList){
      System.out.println(device);
    }
  }

}
