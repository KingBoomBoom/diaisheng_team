package com.qdu.diaisheng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/model")
@Controller
public class DataModelAdminController {
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String ModelList(){
        return "admin/dataModel";
    }
}
