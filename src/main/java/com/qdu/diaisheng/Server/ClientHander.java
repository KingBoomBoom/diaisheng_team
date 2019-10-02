package com.qdu.diaisheng.Server;

import com.qdu.diaisheng.entity.DataPoint;
import com.qdu.diaisheng.entity.DataValue;
import com.qdu.diaisheng.entity.RequestDataConfig;
import com.qdu.diaisheng.util.ByteUtil;
import com.qdu.diaisheng.util.DBMysqlUtil;
import com.qdu.diaisheng.util.UnitUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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

public class ClientHander extends Thread{


        private Socket socket;
        private boolean flag=true;
        public ClientHander(Socket socket){
            this.socket=socket;
        }


        DBMysqlUtil DBUtil =null;
        private   String DBDRIVER = "com.mysql.jdbc.Driver";
        private   String DBURL = "jdbc:mysql://106.12.184.95:3306/diaisheng?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
        private   String DBUSER = "root";
        private   String DBPASSWORD = "diaisheng";

        public void init(){
            DBUtil=new DBMysqlUtil(DBDRIVER,DBURL,DBUSER,DBPASSWORD);
//		 DBUtil=new DBMysqlUtil();
        }






        public long getTimeStamp() throws ParseException {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(new Date().toString());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long timestamp = cal.getTimeInMillis();
            return timestamp;
        }




        public Map<String,Object> getValue(RequestDataConfig requestDataConfig, byte[]results) throws ParseException {


            Map<String,Object>data=new HashMap<String,Object>();
            //data.put("tsp",getTimeStamp());
            int pos=0,flen=0;
            int i=0;
            for(String type:requestDataConfig.getType()){
                System.out.println("columns:"+requestDataConfig.getColumn()[i]);
                pos=pos+flen;

                float v=0;
                switch (type){
                    case "int16":
                        short int16= ByteUtil.getShort(results,pos);
                        System.out.println(int16);
                        v=int16;
                        flen=2;
                        break;
                    case "int32":
                        int int32=ByteUtil.getInt(results,pos);
                        System.out.println(int32);
                        flen=4;
                        v=int32;
                        break;
                    case "int64":
                        long int64=ByteUtil.getLong(results,pos);
                        System.out.println(int64);
                        flen=4;
                        v=int64;
                        break;
                    case "uint16":
                        int unit16;
                        unit16= UnitUtil.getUint16(ByteUtil.getInt(results,pos));
                        System.out.println(unit16);
                        flen=2;
                        break;
                    case "uint32":
                        long unit32;
                        unit32= UnitUtil.getUint32(ByteUtil.getLong(results,pos));
                        System.out.println(unit32);
                        flen=4;
                        break;
                    case "float32":
                        float float32;
                        float32=ByteUtil.getFloat(results,pos);
                        System.out.println(float32);
                        v=float32;
                        flen=4;
                        break;
                    case "float64":
                        double float64;
                        float64=ByteUtil.getDouble(results,pos);
                        flen=8;
                        System.out.println(float64);
                        break;
                    default:
                        System.out.println("error");



                }

                if(requestDataConfig.getColumn()[i]!="NG"){
                    data.put(requestDataConfig.getColumn()[i],v);
                }


                i++;
            }
            //System.out.println(data);
            return data;

        }
        public void addValue(Map<String,Object>data){

            System.out.println("add data begin");

            init();
            String sql="insert into data_value(data_point_id,create_time,val) values(?,?,?)";

            Connection conn=DBUtil.getConnection();
            for(Map.Entry<String,Object>entry:data.entrySet()){
                //     System.out.println("key"+entry.getKey()+":value"+entry.getValue());
                DataValue dataVaule=new DataValue();
                DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format=df.format(new Date());
                dataVaule.setCreateTime(format);
                dataVaule.setValue(Float.parseFloat(entry.getValue().toString()));

                try{
                    PreparedStatement ps=conn.prepareStatement(sql);
                    ps.setString(1,dataVaule.getDataPoint().getDataPointId());
                    ps.setString(2,dataVaule.getCreateTime());
                    ps.setFloat(3,dataVaule.getValue());

                    int effected=ps.executeUpdate();
                    if(effected>0){
                        System.out.println("val:"+dataVaule.getValue()+"插入成功");
                    }


                }catch (Exception e){

                }
            }

            System.out.println("add data end");

        }

        @Override
        public void run(){

            super.run();
            System.out.println("start comm");
            //   for(RequestDataConfig request:requestDataConfig){
            RequestDataConfig request=new RequestDataConfig();
            request=new RequestDataConfig();
            request.setSlavaId(1);
            request.setLength((short) 16);
            request.setMessage(new int[]{0x01, 0x03, 0x00, 0x00, 0x00, 0x0C, 0x45,0xCF});
            request.setType(new String[]{"int16","float32","int32","float32"});
            request.setColumn(new String[]{"32442","30948","30946","30945"});

            System.out.println(request.getSlavaId());

            byte[] sendBuffer=new byte[128];
            byte[] receiveBuffer=new byte[512];
            int cursor=0;
            OutputStream outputStream;
            InputStream inputStream;


            try {
                outputStream=socket.getOutputStream();
                inputStream=socket.getInputStream();


                ByteBuffer byteBuffer=ByteBuffer.wrap(sendBuffer);
                for(int i=0;i<request.getMessage().length;i++){

                    byteBuffer.putInt(request.getMessage()[i]);
                }

                outputStream.write(sendBuffer,0,byteBuffer.position()+1);

                System.out.println("byteBuffer"+byteBuffer+"sendBuffer"+sendBuffer);

                int readCnt=inputStream.read(receiveBuffer);
                System.out.println(readCnt);
                if(readCnt>0){
                    addValue(getValue(request,receiveBuffer));
                }else{
                    System.out.println("can't receive:" + readCnt);
                }

                getValue(request,receiveBuffer);


            }catch (Exception e){

                ///  System.out.println(e.getStackTrace());
            }finally {

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




            //    break;

            // }





        }
}
