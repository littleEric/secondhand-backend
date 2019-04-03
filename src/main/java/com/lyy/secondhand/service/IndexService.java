package com.lyy.secondhand.service;

import com.github.pagehelper.PageHelper;
import com.lyy.secondhand.common.CheckFieldUtil;
import com.lyy.secondhand.common.EntityUtil;
import com.lyy.secondhand.cusexception.EntityNullFieldException;
import com.lyy.secondhand.dto.IndexRequest;
import com.lyy.secondhand.entity.ProductEntity;
import com.lyy.secondhand.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/30
 */
@Service
public class IndexService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    ProductMapper productMapper;

    public List<ProductEntity> getProductByCateBrand(IndexRequest indexRequest){
        System.out.println("IndexService::getProductByCateBrand->"+indexRequest.toString());
        try{
            if (EntityUtil.isFieldNull(indexRequest)){
                throw new EntityNullFieldException("json有空参数");
            }
        }catch (IllegalAccessException e){
            logger.error("IndexService::getProductByCateBrand:ERROR->{}",e.getMessage());
        }

        if (!(CheckFieldUtil.checkBrand(indexRequest.getBrand()) && CheckFieldUtil.checkCategory(indexRequest.getCategory()))){
            logger.error("IndexService::getProductByCateBrand:ERROR{}","参数不合法");
            return null;
        }

        if (indexRequest.getBrand().equals("all")){
            PageHelper.startPage(indexRequest.getPage(),10);    //分页查询，每页10条
            List<ProductEntity> resultList = productMapper.selectByCat(indexRequest.getCategory());
            return resultList;
        }
        PageHelper.startPage(indexRequest.getPage(),10);        //分页查询
        List<ProductEntity> resultList = productMapper.selectByCatBnd(indexRequest.getCategory(),indexRequest.getBrand());
        return resultList;

    }
}
