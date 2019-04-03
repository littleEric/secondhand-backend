package com.lyy.secondhand.controller;

import com.alibaba.fastjson.JSONObject;
import com.lyy.secondhand.service.WxLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class WxLoginController {
    @Autowired
    WxLoginService wxLoginService;

    @RequestMapping(value = "/wxlogin",method = RequestMethod.POST)
    public JSONObject wxLogin(@RequestBody JSONObject json, HttpServletRequest request){
//        System.out.println(json.toJSONString());
        JSONObject jsonObj = wxLoginService.getFromWx(json);
        return jsonObj;
    }

}
