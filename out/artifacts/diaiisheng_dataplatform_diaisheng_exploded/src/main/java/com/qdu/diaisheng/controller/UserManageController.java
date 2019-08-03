package com.qdu.diaisheng.controller;


import com.qdu.diaisheng.entity.DataValue;
import com.qdu.diaisheng.entity.User;
import com.qdu.diaisheng.service.UserService;
import com.qdu.diaisheng.util.HttpServletUtil;
import com.qdu.diaisheng.util.Md5;
import com.sun.tools.internal.ws.processor.model.Model;
import com.sun.tools.javac.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class UserManageController {

    @Autowired
    public UserService userService;

    @RequestMapping(value = "/logincheck")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<String, Object>();
        String userName = HttpServletUtil.getString(request, "userName");
        String password = HttpServletUtil.getString(request, "password");
        String passwordMd5 = Md5.md5(password);
        HttpSession session = request.getSession();
        User user = userService.login(userName, passwordMd5);
        if (user != null) {
            session.setAttribute("nowUser", user);
            modelMap.put("success", true);

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名或密码错误");

        }
        return modelMap;
    }

    @RequestMapping(value = "/adduser")
    @ResponseBody
    public Map<String, Object> add(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        String userName = HttpServletUtil.getString(request, "userName");
        String password = HttpServletUtil.getString(request, "password");
        String passwordMd5 = Md5.md5(password);
        User user = new User();
        user.setPassword(passwordMd5);
        user.setUserName(userName);

        if (userService.findByUserName(userName) == null) {
            if (userService.register(user)) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "注册用户出错");
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "该用户已存在");
        }
        return modelMap;
    }




}