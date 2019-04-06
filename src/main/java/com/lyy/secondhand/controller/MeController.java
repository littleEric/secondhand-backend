package com.lyy.secondhand.controller;

import com.alibaba.fastjson.JSONObject;
import com.lyy.secondhand.aop.AuthToken;
import com.lyy.secondhand.common.ResponseV0;
import com.lyy.secondhand.entity.ProductEntity;
import com.lyy.secondhand.service.ProductService;
import com.lyy.secondhand.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/6
 */
@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //获取用户头像和昵称
    @RequestMapping(value = "/getuserinfo",method = RequestMethod.POST)
    @AuthToken
    public JSONObject getUserInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        return userService.getUserInfo(token);
    }

    //我的页面获取我的发布
    @RequestMapping(value = "/getpublishedlist",method = RequestMethod.POST)
    @AuthToken
    public List<ProductEntity> getPublisedList(HttpServletRequest request){
        String token = request.getHeader("token");
        return productService.getPublishedList(token);
    }

    //获取用户已购买的商品列表
    @RequestMapping(value = "/getboughtlist",method = RequestMethod.POST)
    @AuthToken
    public List<ProductEntity> getBoughtList(HttpServletRequest request){
        String token = request.getHeader("token");
        return productService.getBoughtList(token);
    }

    //获取用户已卖出商品列表
    @RequestMapping(value = "/getsoldlist",method = RequestMethod.POST)
    @AuthToken
    public List<ProductEntity> getSoldList(HttpServletRequest request){
        String token = request.getHeader("token");
        return productService.getSoldList(token);
    }

    //获取用户收藏商品列表
    @RequestMapping(value = "/getstarlist",method = RequestMethod.POST)
    @AuthToken
    public List<ProductEntity> getStarList(HttpServletRequest request){
        String token = request.getHeader("token");
        return productService.getStarList(token);
    }

    //取消发布
    @RequestMapping(value = "/unpublish",method = RequestMethod.POST)
    @AuthToken
    public ResponseV0 unPublish(HttpServletRequest request){
        try {
            Long productId = Long.parseLong(request.getParameter("id"));
            return productService.unPublish(productId);
        }catch (NumberFormatException e){
            logger.error("MeController::unPublish-->{}",e.getMessage());
        }
        return null;
    }
}
