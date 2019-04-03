package com.lyy.secondhand;

import com.lyy.secondhand.common.RedisUtil;
import com.lyy.secondhand.mapper.ProductMapper;
import com.lyy.secondhand.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Mysqltest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ProductMapper productMapper;

    @Test
    public void testSelectCountByOpenId(){
        Assert.assertEquals(new Integer(0),userMapper.selectOneByOpenId("asfdasdf"));
    }

    @Test
    public void testRedis(){
        System.out.println(redisUtil.get("jasdf"));
    }

    @Test
    public void testSelectItemById(){
        System.out.println(productMapper.selectItemById(new Long(1)));
    }

}
