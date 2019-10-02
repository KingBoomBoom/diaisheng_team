package com.qdu.diaisheng.Client;

import com.qdu.diaisheng.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Client {

    private static final int PORT=20000;
    private static final int LOACL_PORT=20001;



    private static Socket createSocket() throws IOException {
        Socket socket=new Socket();
        return socket;
    }




    public static void initSocket(Socket socket) throws SocketException {

        socket.setSoTimeout(3000);

        socket.setReuseAddress(true);

        socket.setKeepAlive(true);

        socket.setSoLinger(true,20);

        socket.setOOBInline(true);

        socket.setReceiveBufferSize(64*1024*1024);
        socket.setSendBufferSize(64*1024*1024);

        socket.setPerformancePreferences(1,1,1);
    }


    public static void main(String[] args) throws IOException {

        Socket socket=createSocket();
        initSocket(socket);

        socket.connect(new InetSocketAddress("106.12.184.95",20001),3000);
        System.out.println("publish connect");
        System.out.println("client:"+socket.getLocalAddress()+"P:"+socket.getLocalPort());
        System.out.println("server:"+socket.getInetAddress()+"P:"+socket.getInetAddress());

        try{
            sendMsg(socket);
        }
        catch (Exception e){
            System.out.println("Exception");
        }


        socket.close();
        System.out.println("client close");

    }

    private static void sendMsg(Socket client)throws IOException{


        OutputStream outputStream=client.getOutputStream();
        PrintStream socketPrintStream=new PrintStream(outputStream);

        InputStream inputStream=client.getInputStream();

        byte[] buffer=new byte[128];
        boolean flag=true;
        int read=inputStream.read(buffer);
        if(read>0){


            ByteBuffer byteBuffer=ByteBuffer.wrap(buffer);
            for(int i=0;i<8;i++){
                System.out.println(byteBuffer.getInt());
            }
            byte[] sendBuffer=new byte[256];
            ByteUtil.putShort(sendBuffer, (short) 1,0);
            ByteUtil.putFloat(sendBuffer, (float) 8.112,2);
            ByteUtil.putInt(sendBuffer,6,6);
            ByteUtil.putFloat(sendBuffer, (float) 6.6,10);
            outputStream.write(sendBuffer);

        }else{
            System.out.println("can't receive:"+read);
        }


        outputStream.close();
        inputStream.close();


    }




}