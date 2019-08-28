package com.qdu.diaisheng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/device")
public class DeviceAdminController {
    @RequestMapping(value = "/location",method = RequestMethod.GET)
    private String dataExport(){
        return "admin/getLocation";
    }
}
