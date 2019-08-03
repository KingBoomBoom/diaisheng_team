package com.qdu.diaisheng.controller;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.qdu.diaisheng.DataValueEnum;
import com.qdu.diaisheng.dto.DataValueExecution;
import com.qdu.diaisheng.entity.DataModel;
import com.qdu.diaisheng.entity.DataPoint;
import com.qdu.diaisheng.entity.DataValue;
import com.qdu.diaisheng.entity.Device;
import com.qdu.diaisheng.service.DataModelService;
import com.qdu.diaisheng.service.DataPointService;
import com.qdu.diaisheng.service.DataValueService;
import com.qdu.diaisheng.service.DeviceService;
import com.qdu.diaisheng.util.HttpServletUtil;
import com.sun.tools.internal.ws.processor.model.Model;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
@Controller
@RequestMapping("/valueadmin")
public class DataValueManagementController {
    @Autowired
    private DataValueService dataValueService;
    @Autowired
    private DataModelService dataModelService;
    @Autowired
    private DataPointService dataPointService;
    @Autowired
    private DeviceService device;
    private Logger logger = Logger.getLogger(this.getClass());



    @RequestMapping(value = "/getdata",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object>getData(){
        String deviceId="00015203000000000001";
        Map<String,Object>modelMap=new HashMap<>();
        DataValueExecution dve=dataValueService.getnowdate(deviceId);
        if(dve.getState()==DataValueEnum.SUCCESS.getState()){
            modelMap.put("data",dve.getDataValueList());
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
        }
        return modelMap;

    }


    @RequestMapping(value = "/getdatabetweendate",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getDataValueBetweenDate(String[] data,String stime,String etime){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        if(data!=null&&etime!=null&&etime!=null){
            stime=stime.replace("T"," ");
           etime=etime.replace("T"," ");
            DataValueExecution dve=dataValueService.getDateValueListAtPointIdBetweenDate(stime,etime, Arrays.asList(data));
            if(dve.getState()== DataValueEnum.SUCCESS.getState()){
                modelMap.put("success",true);
                modelMap.put("dataValue",dve.getDataValueList());
                logger.info("历史记录查询成功");
            }else{
                logger.error("历史记录查询失败！");
                modelMap.put("success",false);
                modelMap.put("errMsg",dve.getStateInfo());
            }
        }else{
            logger.error("历史记录查询失败！pointId为空或者日期为空");
            modelMap.put("success",false);
            modelMap.put("errMsg","pointId为空或者日期为空");
        }
        return modelMap;
    }





    @RequestMapping(value = "/exportdate")
    @ResponseBody
    public Map<String,Object> exportDate(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        String dataPointId = HttpServletUtil.getString(request, "dataPointId");
        String startDate = HttpServletUtil.getString(request, "startDate");
        String endDate = HttpServletUtil.getString(request, "endDate");
        try{
            dataValueService.exportDateValue(dataPointId,startDate,endDate);
        }catch (Exception e){
            throw new RuntimeException("导出数据出错");
        }
        modelMap.put("success",true);
        return modelMap;
    }

    /*
    @RequestMapping(value = "/getnowdatavalue")
    @ResponseBody
    public Map<String,Object> getDataValue(HttpServletRequest request){

        String date=HttpServletUtil.getString(request,"date");
        String pointId=HttpServletUtil.getString(request,"pointId");
        Map<String,Object> modelMap=new HashMap<String, Object>();
        DataValueExecution dve= dataValueService.getnowdate(date,pointId);
        if(dve.getState()== DataValueEnum.SUCCESS.getState()){
            modelMap.put("success",true);
            modelMap.put("dataValue",dve.getDataValueList());
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg",dve.getStateInfo());
        }
        return modelMap;

    }

*/


        @RequestMapping(value = "/downLoadExcel",method =RequestMethod.GET)
        public void downLoadExcel(String[] data,String stime,String etime, HttpServletResponse response) throws IOException{


            System.out.println(data);
            System.out.println(stime);
            System.out.println(etime);
            stime=stime.replace("T"," ");
            etime=etime.replace("T"," ");
            DataValueExecution dve = dataValueService.getDateValueListAtPointIdBetweenDate(stime,etime,Arrays.asList(data));
            if(dve.getState()==DataValueEnum.SUCCESS.getState()){
                List<DataValue>dataValueList=dve.getDataValueList();
                if ( dataValueList!= null && dataValueList.size() > 0) {

                    String fileName = "value.xlsx";
                    response.setHeader("Content-disposition", "attachment;filename="
                            + new String(fileName.getBytes("gb2312"), "ISO8859-1"));//设置文件头编码格式
                    response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型
                    response.setHeader("Cache-Control", "no-cache");//设置头
                    response.setDateHeader("Expires", 0);//设置日期头

                    XSSFWorkbook workbook = new XSSFWorkbook();

                    XSSFSheet sheet = workbook.createSheet();
                    CellStyle cellStyle = workbook.createCellStyle();

                    cellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

                    Row header=sheet.createRow(0);
                    for(int cellNum=0;cellNum<4;cellNum++){
                        Cell cell=header.createCell(cellNum);

                        if(cellNum==0){
                            cell.setCellValue("pointName");
                        }else if(cellNum==1){
                            cell.setCellValue("pointId");
                        }else if(cellNum==2){
                            cell.setCellValue("createTime");
                        }else{
                            cell.setCellValue("value");
                        }

                    }

                    int rowNum = 1;
                    for (DataValue dataValue:dataValueList) {
                        Row row = sheet.createRow(rowNum);

                        Cell cell=row.createCell(0);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(dataValue.getDataPoint().getDataPointName());

                        Cell cell1 = row.createCell(1);
                        cell1.setCellStyle(cellStyle);
                        cell1.setCellValue(dataValue.getDataPoint().getDataPointId());

                        Cell cell2 = row.createCell(2);
                        cell2.setCellValue(dataValue.getCreateTime());

                        Cell cell3 = row.createCell(3);
                        cell3.setCellValue(dataValue.getValue());

                        rowNum++;
                    }

                    workbook.write(response.getOutputStream());

                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                }
            }
        }






}
