package com.lyy.secondhand.service;

import com.lyy.secondhand.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //查找用户是否存在
    public boolean exisitUser(String openId){
        if (userMapper.selectOneByOpenId(openId) > 1){
            logger.error("the fucking count of OpenId ({}) > 1!!!!!", openId);
        }
        return (userMapper.selectOneByOpenId(openId) == 1);
    }
}
