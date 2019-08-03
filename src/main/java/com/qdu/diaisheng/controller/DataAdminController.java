package com.qdu.diaisheng.controller;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/data")
public class DataAdminController {
    Logger logger = Logger.getLogger(this.getClass());
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    private String index(){
        return "admin/login";
    }
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    private String test(){
        return "admin/test";
    }
    @RequestMapping(value = "/dataexport",method = RequestMethod.GET)
    private String dataExport(){
        return "admin/table-export";
    }
    /**
     * @Author changliang
     * @Description data-export主页面，即除了导航栏之外的页面
     * @Date 2019/8/2 17:38
     * @Param []
     * @return
     **/
    @RequestMapping(value = "/dataexport2",method = RequestMethod.GET)
    private String dataExport2(){
        return "admin/table-export2";
    }
    /**
     * @Author changliang
     * @Description 当打开主界面时，首先验证是否登录过，没登录过返回登录界面
     * @Date 2019/7/21 23:35
     * @Param [request, response]
     * @return
     **/
    @RequestMapping(value = "/list")
    private String dataList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //编码规范
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        //获取session值
        HttpSession session = request.getSession();
        Object user = session.getAttribute("loginUser");
        try {
            if (user!=null){
                logger.info("登录成功！");
                return "redirect:/data/list2";
            }else{
                return "redirect:/admin/login";
            }
        }catch (Exception e){
            logger.error("session获取user异常！",e);
            return "redirect:/admin/login";
        }
    }
    /**
     * @Author changliang
     * @Description 当用户登录成功时，跳转到List界面
     * @Date 2019/7/24 22:14
     * @Param [request, response]
     * @return
     **/
    @RequestMapping(value = "/list2")
    private String dataList2(HttpServletRequest request, HttpServletResponse response){
        return "admin/List";
    }
}
