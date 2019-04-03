package com.lyy.secondhand.controller;

import com.lyy.secondhand.aop.AuthToken;
import com.lyy.secondhand.common.EntityUtil;
import com.lyy.secondhand.dto.IndexRequest;
import com.lyy.secondhand.entity.ProductEntity;
import com.lyy.secondhand.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @Date: 2019/3/30
 */

@RestController
@RequestMapping("/")
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/index/productlist",method = RequestMethod.POST)
    @AuthToken
    public List<ProductEntity> getProductList(@RequestBody IndexRequest indexRequest, HttpServletRequest request) {
        List<ProductEntity> result = indexService.getProductByCateBrand(indexRequest);
        for (ProductEntity productEntity : result) {
            try {
                EntityUtil.filterProductItem(productEntity.getUserEntity());
            } catch (IllegalAccessException e) {
                logger.error("IndexController::getProductList--->{}", e.getMessage());
            }
        }
        return result;
    }
}
