package com.qdu.diaisheng.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/admin")
public class IndexAdminController {

    @RequestMapping(value = "/index")
    public String index(){
        return "admin/indx";
    }

}
