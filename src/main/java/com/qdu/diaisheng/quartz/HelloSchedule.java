package com.qdu.diaisheng.quartz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdu.diaisheng.service.PhotoService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloSchedule {



    public static void main(String[] args) throws SchedulerException, IOException {
        //创建一个JobDetail实例




        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("myjob", "group1").build();
        //创建一个Trigger实例,用来触发Job执行
        //定义该Job立即执行，并且每隔两分钟执行一次，直到永远
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1").startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever()).build();
         //创建Scheduler实例
        SchedulerFactory sfact=new StdSchedulerFactory();
        Scheduler scheduler=sfact.getScheduler();
        System.out.println("---------schedule start---------");
        scheduler.start();
        scheduler.scheduleJob(jobDetail,trigger);


    }
}
