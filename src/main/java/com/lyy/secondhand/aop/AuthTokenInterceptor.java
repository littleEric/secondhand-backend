package com.lyy.secondhand.aop;

import com.lyy.secondhand.common.RedisUtil;
import com.lyy.secondhand.cusexception.TokenErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
public class AuthTokenInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtil redisUtil;

    private final Logger logger = LoggerFactory.getLogger(AuthTokenInterceptor.class);

    private static final String authFieldName = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");//取出header中的token
        System.out.println("AuthTokenInterceptor::preHandle----->"+token);
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //校验方法体是否带AuthToken注释
        if (method.isAnnotationPresent(AuthToken.class)){
            //如果token为null，抛出异常
            if (token == null){
                throw new TokenErrorException("非法访问");
            }
            if (redisUtil.getExpire(token) == -2){       //过期时间小于5s
                throw new TokenErrorException("token不存在");
            }
        }
        return true;
    }
}
