package com.lyy.secondhand.controller;

import com.alibaba.fastjson.JSONObject;
import com.lyy.secondhand.aop.AuthToken;
import com.lyy.secondhand.common.ResponseV0;
import com.lyy.secondhand.entity.AddressEntity;
import com.lyy.secondhand.entity.DomAreaEntity;
import com.lyy.secondhand.entity.LocationEntity;
import com.lyy.secondhand.entity.OrderEntity;
import com.lyy.secondhand.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/4
 */
@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/address/add",method = RequestMethod.POST)
    @AuthToken
    public ResponseV0<String> addAddress(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        System.out.println(jsonObject.toString());
        String token = request.getHeader("token");
        return orderService.addAddress(jsonObject,token);
    }

    @RequestMapping(value = "/address/getlist",method = RequestMethod.POST)
    @AuthToken
    public List<AddressEntity> getAddressList(HttpServletRequest request){
        String token = request.getHeader("token");
        return orderService.getAddressList(token);
    }

    @RequestMapping(value = "/address/getlocationlist",method = RequestMethod.POST)
    public List<LocationEntity> getLocationList(){
        return orderService.getLocationList();
    }

    @RequestMapping(value = "/address/getdomarealist",method = RequestMethod.POST)
    public List<DomAreaEntity> getDomAreaList(){
        return orderService.getDomAreaList();
    }

    @RequestMapping(value = "/order/add",method = RequestMethod.POST)
    @AuthToken
    public ResponseV0<String> addOrder(@RequestBody OrderEntity orderEntity, HttpServletRequest request){
        String token = request.getHeader("token");
        System.out.println(orderEntity.toString());
        return orderService.addOrder(orderEntity,token);
    }
}
