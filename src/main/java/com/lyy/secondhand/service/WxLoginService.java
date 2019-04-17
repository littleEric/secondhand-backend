package com.lyy.secondhand.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyy.secondhand.common.RedisUtil;
import com.lyy.secondhand.cusexception.CodeFromFrontErrorException;
import com.lyy.secondhand.entity.UserEntity;
import com.lyy.secondhand.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class WxLoginService {
    @Value("${wx.appid}")
    private String APPID;

    @Value("${wx.secret}")
    private String SECRET;

    @Value("${wx.grant_type}")
    private String GRANT_TYPE;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(WxLoginService.class);

    private boolean createUser(JSONObject userInfo,String openID){
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userInfo.getString("nickName"));
        userEntity.setGender(userInfo.getInteger("gender"));
        userEntity.setLanguage(userInfo.getString("language"));
        userEntity.setCity(userInfo.getString("city"));
        userEntity.setProvince(userInfo.getString("province"));
        userEntity.setAvatarUrl(userInfo.getString("avatarUrl"));
        userEntity.setCountry(userInfo.getString("country"));
        userEntity.setOpenId(openID);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        userEntity.setCreateTime(timestamp);
//        userEntity.setUpdateTime(timestamp);

        logger.info("UserEntity to insert: {}", JSON.toJSONString(userEntity));
//        return userService.addUser(userEntity);
        return userMapper.insertUser(userEntity) > 0;
    }

    private JSONObject get3rdSession(String openID){
        String _3rd_session = UUID.randomUUID().toString().replaceAll("-","");
        //token在redis中保存30天
        redisUtil.setEx(_3rd_session,openID,30,TimeUnit.DAYS);
        JSONObject sessionJson = new JSONObject();
        sessionJson.put("_3rd_session",_3rd_session);
        return sessionJson;
    }

    public JSONObject getFromWx(JSONObject json){
        String code = json.getString("code");
        String url = "https://api.weixin.qq.com/sns/jscode2session"
                +"?appid="+APPID
                +"&secret="+SECRET
                +"&js_code="+code
                +"&grant_type="+GRANT_TYPE;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        JSONObject dataFromTencent = JSON.parseObject(responseEntity.getBody());
//        System.out.println("WxLoginService::getFromWx=>"+dataFromTencent.toJSONString());
//        System.out.println("WxLoginService::getFromWx=>"+dataFromTencent.getString("openid"));

        //若返回值中没有open_id字段，抛出异常，全局异常捕获，返回非法访问响应
        if(null==dataFromTencent.getString("openid")){
            throw new CodeFromFrontErrorException("非法访问");
        }
        String openid = dataFromTencent.getString("openid");
        JSONObject _3rd_session = null;
        //若存在该openid，返回3rd_session，存入redis
        if(userService.exisitUser(openid)){
            //将session存入redis,有效时间30分钟，openid=>_3rd_session
            _3rd_session =  get3rdSession(openid);
        }else {
            createUser(json.getJSONObject("userInfo"),openid);
            _3rd_session =  get3rdSession(openid);
        }
        return _3rd_session;

    }
}
