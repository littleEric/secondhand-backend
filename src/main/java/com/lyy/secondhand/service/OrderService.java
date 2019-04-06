package com.lyy.secondhand.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lyy.secondhand.common.EntityUtil;
import com.lyy.secondhand.common.RedisUtil;
import com.lyy.secondhand.common.ResponseStrEnum;
import com.lyy.secondhand.common.ResponseV0;
import com.lyy.secondhand.entity.*;
import com.lyy.secondhand.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/4
 */
@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    DomAreaMapper domAreaMapper;

    @Autowired
    LocationMapper locationMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductMapper productMapper;

    public ResponseV0<String> addAddress(JSONObject jsonObject, String token){
        String location = jsonObject.getString("location");
        String domArea = jsonObject.getString("domArea");
        Long domId = domAreaMapper.selectOne(new QueryWrapper<DomAreaEntity>().eq("name",domArea)).getId();
        Long locationId = locationMapper.selectOne(new QueryWrapper<LocationEntity>().eq("name",location)).getId();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setIfDefault(0);
        addressEntity.setOpenId(redisUtil.get(token));
        addressEntity.setBuildingNum(jsonObject.getString("buildingNum"));
        addressEntity.setDomAreaId(domId);
        addressEntity.setLocationId(locationId);
        addressEntity.setPhoneNum(jsonObject.getString("phoneNum"));
        addressEntity.setName(jsonObject.getString("name"));
        addressEntity.setRoomNum(jsonObject.getString("roomNum"));
        //空字段白名单
        Set<String> whiteList = new HashSet<String>()
        {{
            add("id");
        }};
        //校验空字段
        try {
            if (EntityUtil.isFieldNullByList(addressEntity,whiteList)){
                return new ResponseV0<>(ResponseStrEnum.ADDRESS_ADD_FAILED,"",ResponseStrEnum.ADDRESS_ADD_FAILED.getMsg());
            }
        }catch (IllegalAccessException e){
            logger.error("OrderService::{}",e.getMessage());
        }
        if (addressMapper.insert(addressEntity) == 1){
            return new ResponseV0<>(ResponseStrEnum.ADDRESS_ADD_SUCCESS,"",ResponseStrEnum.ADDRESS_ADD_SUCCESS.getMsg());
        }
        if (addressMapper.insert(addressEntity) > 1){
            logger.error("OrderService::addAddress-->{}","insert > 1");
        }
        return new ResponseV0<>(ResponseStrEnum.ADDRESS_ADD_FAILED,"",ResponseStrEnum.ADDRESS_ADD_FAILED.getMsg());
    }

    public List<AddressEntity> getAddressList(String token){
        String openId = redisUtil.get(token);
        List<AddressEntity> addressEntities = addressMapper.selectList(new QueryWrapper<AddressEntity>().eq("open_id",openId));
        return addressEntities;
    }

    public List<LocationEntity> getLocationList(){
        List<LocationEntity> locationEntities = locationMapper.selectList(new QueryWrapper<LocationEntity>());
        return locationEntities;
    }

    public List<DomAreaEntity> getDomAreaList(){
        List<DomAreaEntity> domAreaEntities = domAreaMapper.selectList(new QueryWrapper<DomAreaEntity>());
        Set<String> whiteList = new HashSet<String>(){{
            add("openId");
        }};
        try {
            for (DomAreaEntity domAreaEntity:domAreaEntities){
                EntityUtil.fieldFilter(domAreaEntity,whiteList);
            }
        }catch (IllegalAccessException e){
            logger.error("OrderService::getDomAreaList--->{}",e.getMessage());
        }
        return domAreaEntities;
    }

    public ResponseV0<String> addOrder(OrderEntity orderEntity,String token){
        //openId
        String openId = redisUtil.get(token);
        orderEntity.setStatus(1);
        orderEntity.setBuyerOpenId(openId);
        System.out.println(orderEntity);
        Set<String> whiteList = new HashSet<String>(){{
           add("id");
           add("createTime");
           add("updateTime");
        }};
        try{
            if (EntityUtil.isFieldNullByList(orderEntity,whiteList)){
                return new ResponseV0<String>(ResponseStrEnum.ADD_ORDER_FAILED,"",ResponseStrEnum.ADD_ORDER_FAILED.getMsg());
            }
        }catch (IllegalAccessException e){
            logger.error("OrderService::addOrder--->{}",e.getMessage());
        }
        if (orderMapper.insertOrder(orderEntity) > 1){
            logger.error("OrderService::addOrder::insert--->{}"," > 1");
            return new ResponseV0<String>(ResponseStrEnum.ADD_ORDER_FAILED,"",ResponseStrEnum.ADD_ORDER_FAILED.getMsg());
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setStatus(new Byte("1"));
        //修改商品状态
        if (productMapper.update(productEntity,new UpdateWrapper<ProductEntity>().eq("open_id",openId).eq("id",orderEntity.getProductId())) > 1){
            logger.error("OrderService::addOrder::update--->{}"," > 1");
            return new ResponseV0<String>(ResponseStrEnum.ADD_ORDER_FAILED,"",ResponseStrEnum.ADD_ORDER_FAILED.getMsg());
        }
        return new ResponseV0<String>(ResponseStrEnum.ADD_ORDER_SUCCESS,"",ResponseStrEnum.ADD_ORDER_SUCCESS.getMsg());
    }
}
