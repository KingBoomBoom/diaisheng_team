package com.qdu.diaisheng.Server;

import com.qdu.diaisheng.dao.DataModelDao;
import com.qdu.diaisheng.dao.DataPointDao;
import com.qdu.diaisheng.dao.DataVauleDao;
import com.qdu.diaisheng.entity.DataModel;
import com.qdu.diaisheng.entity.DataPoint;
import com.qdu.diaisheng.entity.DataValue;
import com.qdu.diaisheng.entity.RequestDataConfig;
import com.qdu.diaisheng.util.ByteUtil;

import com.qdu.diaisheng.util.DBMysqlUtil;
import com.qdu.diaisheng.util.UnitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import sun.tools.jconsole.Plotter;


import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Srever {



    public static int PORT=20001;
    public static RequestDataConfig requestDataConfig[]=new RequestDataConfig[5];

    public static ServerSocket createSocket() throws IOException {
        ServerSocket socketServer=new ServerSocket();

        return socketServer;
    }

    public void initRequestConfig(){

    }






    public static void initSocket(ServerSocket serverSocket) throws SocketException {
        serverSocket.setReuseAddress(true);
        serverSocket.setReceiveBufferSize(6*1024*1024);
        serverSocket.setPerformancePreferences(2,2,1);
    }


    public static void main(String[] args) throws IOException {

        requestDataConfig[0]=new RequestDataConfig();
       requestDataConfig[0].setSlavaId(1);
       requestDataConfig[0].setLength((short) 16);
       requestDataConfig[0].setMessage(new int[]{0x01, 0x03, 0x00, 0x00, 0x00, 0x0C, 0x45,0xCF});
       requestDataConfig[0].setType(new String[]{"int16","float32","int32","float32"});
       requestDataConfig[0].setColumn(new String[]{"32442","30948","30946","30945"});

        requestDataConfig[1]=new RequestDataConfig();
        requestDataConfig[1].setSlavaId(2);
        requestDataConfig[1].setLength((short) 20);
        requestDataConfig[1].setMessage(new int[]{0x02, 0x03, 0x00, 0x00, 0x00, 0x04, 0x44,0x3A});
        requestDataConfig[1].setType(new String[]{"uint16","uint16","uint32","uint16"});
        requestDataConfig[1].setColumn(new String[]{"","","","",""});

        requestDataConfig[2]=new RequestDataConfig();
        requestDataConfig[2].setSlavaId(3);
        requestDataConfig[2].setLength((short) 20);
        requestDataConfig[2].setMessage(new int[]{0x03, 0x03, 0x00, 0x00, 0x00, 0x0C, 0x44,0x4B});
        requestDataConfig[2].setType(new String[]{"uint16","uint16","uint32","uint16"});
        requestDataConfig[2].setColumn(new String[]{"","","","",""});


        requestDataConfig[3]=new RequestDataConfig();
        requestDataConfig[3].setSlavaId(4);
        requestDataConfig[3].setLength((short) 20);
        requestDataConfig[3].setMessage(new int[]{0x04, 0x03, 0x00, 0x00, 0x00, 0x0C,0x44,0x4B});
        requestDataConfig[3].setType(new String[]{"uint16","uint16","uint32","uint16"});
        requestDataConfig[3].setColumn(new String[]{"","","","",""});


        requestDataConfig[4]=new RequestDataConfig();
        requestDataConfig[4].setSlavaId(5);
        requestDataConfig[4].setLength((short) 20);
        requestDataConfig[4].setMessage(new int[]{0x05, 0x03, 0x00, 0x00, 0x00, 0x0C, 0x45,0xCF});
        requestDataConfig[4].setType(new String[]{"uint16","uint16","uint32","uint16"});
        requestDataConfig[4].setColumn(new String[]{"","","","",""});


       ServerSocket serverSocket=createSocket();
       initSocket(serverSocket);
       serverSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(),PORT),50);

       System.out.println("start server successful......");
       while (true){
           Socket clientSocket=serverSocket.accept();
           System.out.println("accept tcp client "+clientSocket.getRemoteSocketAddress().toString());
           ClientHander clientHander=new ClientHander(clientSocket);
           clientHander.start();
       }



    }






}
