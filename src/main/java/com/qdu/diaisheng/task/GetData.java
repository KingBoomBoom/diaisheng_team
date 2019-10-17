package com.qdu.diaisheng.task;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;

@Component
public class GetData {
    public static int PORT=20001;
    private Logger logger = Logger.getLogger(this.getClass());

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
    @Scheduled(cron = "0 */2 * * * ?")
    public void queryDatasFromDtu(){
        try{
            ServerSocket serverSocket=createSocket();//给云服务器制定端口，初始化
            logger.info("监听端口成功...");
            initSocket(serverSocket);//连接属性初始化设置
            serverSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(),PORT),50);
            logger.info("start server successful......"+Inet4Address.getLocalHost().toString());
            Socket clientSocket=serverSocket.accept();// 如果有请求到达，则接受请求，并建立一个新的套接字
            logger.info("accept tcp client "+clientSocket.getRemoteSocketAddress().toString());
            logger.info("本地ip为:"+clientSocket.getLocalAddress().toString()+"远程ip地址为："+clientSocket.getRemoteSocketAddress().toString());
            ClientHander hander=new ClientHander(clientSocket);
            hander.start();
        }catch (Exception e){
            logger.error("spring定时任务出错！",e);
        }

    }


}
