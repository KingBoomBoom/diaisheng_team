package com.qdu.diaisheng.service;

import com.qdu.diaisheng.entity.DataPoint;
import java.util.List;
import org.springframework.stereotype.Service;


public interface DataPointService {

    List<DataPoint> getDataPointListByDataModelId(int dataModelId);

    String getDataPointName(String pointId);
}
