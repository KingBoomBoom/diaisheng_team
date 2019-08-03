package com.qdu.diaisheng.dao;

import com.qdu.diaisheng.entity.DataPoint;
import java.util.List;

public interface DataPointDao {
    int insertDataPoint(DataPoint dataPoint);
    List<DataPoint> queryDataPointListByDataModel(Integer dataModelId);
    String getPointNameByPointId(String pointId);
}
