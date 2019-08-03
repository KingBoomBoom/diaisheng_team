package com.qdu.diaisheng.service.impl;

import com.qdu.diaisheng.DataValueEnum;
import com.qdu.diaisheng.dao.DataVauleDao;
import com.qdu.diaisheng.dto.DataValueExecution;
import com.qdu.diaisheng.entity.DataValue;


import java.util.List;

import com.qdu.diaisheng.service.DataValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DataValueServiceImpl implements DataValueService {

    @Autowired
    private DataVauleDao dataValueDao;


    /**
     * 向数据库中添加数据
     * @param dataValue
     * @return
     */
    @Override
    @Transactional
    public DataValueExecution addDataValue(DataValue dataValue) {
        if(dataValue!=null&&dataValue.getDataPoint()!=null&&dataValue.getDataPoint().getDataPointId()!=null){
            int effectedNum=dataValueDao.insertDataVaule(dataValue);
            if (effectedNum<0){
                throw new RuntimeException("插入数据错误");
            }

            return new DataValueExecution(DataValueEnum.SUCCESS,dataValue);

        }else {
            return new DataValueExecution(DataValueEnum.PAR_EMPTY);
        }
    }

    /**
     * 通过数据点Id来查询数据列表
     * @param ponitId
     * @return
     */
    /*
    @Override
    public DataValueExecution getDataValueListByPointId(String ponitId) {
        DataValueExecution dve=new DataValueExecution();

        if(ponitId!=null){
            List<DataValue> dataValueList =dataValueDao.queryByDataPointId(ponitId);
            if(dataValueList!=null){
                dve.setDataValueList(dataValueList);
                dve.setCount(dataValueList.size());
                dve.setState(DataValueEnum.SUCCESS.getState());
            }
            else{
                dve.setState(DataValueEnum.EMPTY.getState());
            }
        }else{
            return new DataValueExecution(DataValueEnum.PAR_EMPTY);
        }
        return dve;
    }
 */
    /**
     *
     * 通过deviceId和日期来获取数据
     * @param date
     * @param deviceId
     * @return
     */
    @Override
    public DataValueExecution getDataValueByDeviceAndDate(String date, String deviceId) {
        DataValueExecution dve=new DataValueExecution();
        if(date!=null&&deviceId!=null){
            List<DataValue> dataValueList= dataValueDao.getAllByDeviceAndDate(date,deviceId);
            if(dataValueList!=null){
                dve.setDataValueList(dataValueList);

                dve.setState(DataValueEnum.SUCCESS.getState());
            }

        }else{
            return new DataValueExecution(DataValueEnum.PAR_EMPTY);
        }
        return dve;
    }

    /**
     *
     * 通过日期来获取数据
     * @param date
     * @return
     */
    @Override
    public List<DataValue> getDataValueListByDate(String date) {
        return dataValueDao.queryByDate(date);
    }

    /**
     *
     * 通过数据点Id来获取某个数据点下两个日期之间的数据
     * @param
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DataValueExecution getDateValueListAtPointIdBetweenDate(String date1,String date2,String pointId) {
        DataValueExecution dve=new DataValueExecution();

        if(date1!=null&&date2!=null&&pointId!=null){
            List<DataValue> dataValueList =dataValueDao.queryBetweenDateAndPonitId(date1,date2,pointId);
            if(dataValueList!=null){
                dve.setDataValueList(dataValueList);
                dve.setCount(dataValueList.size());
                dve.setState(DataValueEnum.SUCCESS.getState());
            }
            else{
                dve.setState(DataValueEnum.EMPTY.getState());
            }
        }else{
            return new DataValueExecution(DataValueEnum.PAR_EMPTY);
        }
        return dve;
    }

    @Override
    public void exportDateValue(String pointId, String startDate, String endDate) {
        dataValueDao.exportDataValue(pointId,startDate,endDate);
    }

}
