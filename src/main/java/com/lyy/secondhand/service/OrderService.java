package com.lyy.secondhand.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lyy.secondhand.common.EntityUtil;
import com.lyy.secondhand.common.RedisUtil;
import com.lyy.secondhand.common.ResponseStrEnum;
import com.lyy.secondhand.common.ResponseV0;
import com.lyy.secondhand.cusexception.EntityNullFieldException;
import com.lyy.secondhand.entity.*;
import com.lyy.secondhand.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    UserService userService;

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

    //获取当前用户地址列表
    public List<AddressEntity> getAddressList(String token){
        String openId = redisUtil.get(token);
        List<AddressEntity> addressEntities = addressMapper.selectList(new QueryWrapper<AddressEntity>().eq("open_id",openId));
        return addressEntities;
    }
    //获取校区列表
    public List<LocationEntity> getLocationList(){
        List<LocationEntity> locationEntities = locationMapper.selectList(new QueryWrapper<LocationEntity>());
        return locationEntities;
    }
    //获取宿舍园区列表
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

    //生成订单
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

    //查询订单详情
    public Map<String,Object> getOrderDetails(String productId){
        if (productId.equals("")){
            throw new EntityNullFieldException("订单查询：非法请求！");
        }
        Long _productId = null;
        try{
            _productId = Long.parseLong(productId);
        }catch (NumberFormatException e){
//            return new ResponseV0<String>(ResponseStrEnum.ORDER_DETAIL_FAILED,"",ResponseStrEnum.ORDER_DETAIL_FAILED.getMsg());
            return null;
        }
        Map<String,Object> result = new HashMap<>();
        //当前商品信息
        ProductEntity productEntity = productMapper.selectById(_productId);
        //订单地址id
        OrderEntity orderEntity = orderMapper.selectOne(new QueryWrapper<OrderEntity>().eq("product_id",_productId).select("address_id"));
        //根据订单id查询买家地址详情
        AddressEntity addressEntity = addressMapper.selectById(orderEntity.getAddressId());
        //根据校区id查校区
        addressEntity.setLocation(locationMapper.selectById(addressEntity.getLocationId()).getName());
        //根据园区id查园区
        addressEntity.setDomArea(domAreaMapper.selectById(addressEntity.getDomAreaId()).getName());
        //根据openId获取卖家头像昵称
        JSONObject avaNname = userService.getUserInfoByOpenId(productEntity.getOpenId());
        Set<String> whiteList = new HashSet<>();
        whiteList.add("openId");
        try {
            EntityUtil.fieldFilter(productEntity,whiteList);
            EntityUtil.fieldFilter(addressEntity,whiteList);
        }catch (IllegalAccessException e){
            logger.error(e.getMessage());
            return null;
        }
        result.put("product",productEntity);
        result.put("address",addressEntity);
        result.put("avaNname",avaNname);
        return result;
    }
}
