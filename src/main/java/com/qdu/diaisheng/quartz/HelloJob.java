package com.qdu.diaisheng.quartz;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdu.diaisheng.service.PhotoService;
import com.qdu.diaisheng.task.Server;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloJob implements Job {

    private Logger logger = Logger.getLogger(this.getClass());

    public static int PORT=20001;


    @Autowired
    private PhotoService photoService;

    public static ServerSocket serverSocket;
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
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //打印当前的执行时间
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("current time:"+sdf.format(date)+"job");

        try{
            ServerSocket serverSocket=createSocket();//给云服务器制定端口，初始化
            initSocket(serverSocket);//连接属性初始化设置
            serverSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(),PORT),50);
            logger.info("监听端口成功...");
            logger.info("start server successful......"+Inet4Address.getLocalHost().toString());
            Socket clientSocket=serverSocket.accept();// 如果有请求到达，则接受请求，并建立一个新的套接字
            logger.info("accept tcp client "+clientSocket.getRemoteSocketAddress().toString());
            logger.info("本地ip为:"+clientSocket.getLocalAddress().toString()+"远程ip地址为："+clientSocket.getRemoteSocketAddress().toString());
            com.qdu.diaisheng.task.ClientHander hander=new com.qdu.diaisheng.task.ClientHander(clientSocket);
            hander.start();
        }catch (Exception e){
            logger.error("spring定时任务出错！",e);
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
