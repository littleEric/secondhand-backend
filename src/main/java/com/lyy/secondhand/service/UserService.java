package com.lyy.secondhand.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyy.secondhand.common.RedisUtil;
import com.lyy.secondhand.entity.UserEntity;
import com.lyy.secondhand.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //查找用户是否存在
    public boolean exisitUser(String openId){
        if (userMapper.selectOneByOpenId(openId) > 1){
            logger.error("the fucking count of OpenId ({}) > 1!!!!!", openId);
        }
        return (userMapper.selectOneByOpenId(openId) == 1);
    }

    //获取用户头像和昵称
    public JSONObject getUserInfo(String token){
        if (redisUtil.getExpire(token)!= -2){
            String openId = redisUtil.get(token);
            UserEntity userEntity = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id",openId));
            JSONObject userInfo = new JSONObject();
            userInfo.put("avatarUrl",userEntity.getAvatarUrl());
            userInfo.put("name",userEntity.getName());
            return userInfo;
        }
        logger.error("UserService::getUserInfo--->{}","非法访问");  //token不存在或者已过期
        return null;
    }

    //根据open_id获取用户头像和昵称
    public JSONObject getUserInfoByOpenId(String openId){
        UserEntity userEntity = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id",openId));
        JSONObject userInfo = new JSONObject();
        userInfo.put("avatarUrl",userEntity.getAvatarUrl());
        userInfo.put("name",userEntity.getName());
        return userInfo;
    }


}
