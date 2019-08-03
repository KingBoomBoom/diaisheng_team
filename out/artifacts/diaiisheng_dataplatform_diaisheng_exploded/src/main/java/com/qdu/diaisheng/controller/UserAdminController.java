package com.qdu.diaisheng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class UserAdminController {
    @RequestMapping(value = "/login")
    private String login(){
        return "admin/login";
    }
}
