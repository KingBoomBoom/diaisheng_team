package com.qdu.diaisheng.task;


import java.io.IOException;
import java.net.*;

public class Server {
    public static int PORT=20001;

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
       ServerSocket serverSocket=createSocket();//给云服务器制定端口，初始化
        System.out.println("监听端口成功...");
       initSocket(serverSocket);//连接属性初始化设置
       serverSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(),PORT),50);
       System.out.println("start server successful......"+Inet4Address.getLocalHost().toString());
           
           while(true){
               Socket clientSocket=serverSocket.accept();// 如果有请求到达，则接受请求，并建立一个新的套接字
               System.out.println("accept tcp client "+clientSocket.getRemoteSocketAddress().toString());
               System.out.println("本地ip为:"+clientSocket.getLocalAddress().toString()+"远程ip地址为："+clientSocket.getRemoteSocketAddress().toString());
              com.qdu.diaisheng.task.ClientHander hander=new com.qdu.diaisheng.task.ClientHander(clientSocket);
              hander.start();
           }
      
    }
    
 
    
}
