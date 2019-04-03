package com.lyy.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyy.secondhand.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<UserEntity> {

     //根据openId查找用户
     Integer selectOneByOpenId(String openid);

     Integer insertUser(UserEntity userEntity);

     UserEntity selectByOpenId(String openId);
}
