package com.qdu.diaisheng.dao;

import com.qdu.diaisheng.BaseTest;
import com.qdu.diaisheng.entity.DataModel;
import com.qdu.diaisheng.entity.Device;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class DataModelTest extends BaseTest {
    @Autowired
    private DataModelDao dataModelDao;

    @Test
    @Ignore
    public void testInsertDataModel(){
        DataModel dataModel=new DataModel();
        dataModel.setDataModelName("SOD");
        Device device=new Device();
        device.setDeviceId("0000000001");
        dataModel.setDevice(device);
        //dataModel.setCreateTime(new Date());
        int effectedNum=dataModelDao.insertDataModel(dataModel);
        System.out.println(effectedNum);
    }

    @Test
    public void testQueryDataModelByDeviceId(){
        String deviceId="00015203000000000001";
        List<DataModel>dataModelList=dataModelDao.queryDataModelByDeviceId(deviceId);
        for(DataModel dataModel:dataModelList){
            System.out.println(dataModel);
        }
    }
}
