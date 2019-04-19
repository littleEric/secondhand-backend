package com.lyy.secondhand.controller;

import com.lyy.secondhand.aop.AuthToken;
import com.lyy.secondhand.common.ResponseStrEnum;
import com.lyy.secondhand.dto.Product;
import com.lyy.secondhand.common.ResponseV0;
import com.lyy.secondhand.dto.ProductItem;
import com.lyy.secondhand.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/publish/uploadpic",method = RequestMethod.POST)
    @AuthToken
    public String upload(@RequestParam("file") MultipartFile file,HttpServletRequest request){
        System.out.println("PublishController:upload::token->"+request.getHeader("token"));
        String result = productService.saveFile(file);
        return result;
    }

    @RequestMapping(value = "/publish/add",method = RequestMethod.POST,consumes = "application/json")
    @AuthToken
    public ResponseV0<String> add(@RequestBody Product product, HttpServletRequest request){
        return productService.addProduct(product);
    }

    @RequestMapping(value = "/getiteminfo",method = RequestMethod.POST)
    @AuthToken
    public ProductItem getItemInfo(HttpServletRequest request){
        Long productId = Long.parseLong(request.getParameter("id"));
        System.out.println("ProductController::getItemInfo---->>"+productId);
        String token = request.getHeader("token");
        return productService.selectItem(productId,token);
    }

    @RequestMapping(value = "/updateStar",method = RequestMethod.POST)
    @AuthToken
    public ResponseV0<String> updateStar(HttpServletRequest request){
        try{
            //获取token
            String token = request.getHeader("token");
            Long productId = Long.parseLong(request.getParameter("id"));
            Integer action = Integer.parseInt(request.getParameter("action"));
            return productService.updateStar(token,productId,action);
        }catch (Exception e){
            logger.error("ProductController::updateStar{}","传入参数错误");
        }

//        System.out.println("updateStar-->>"+token+"::::"+productId+"::::"+action);
        return new ResponseV0<>(ResponseStrEnum.STAR_FAILED,"",ResponseStrEnum.STAR_FAILED.getMsg());
    }

}
