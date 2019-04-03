package com.lyy.secondhand.common;

import com.lyy.secondhand.cusexception.CodeFromFrontErrorException;
import com.lyy.secondhand.cusexception.EntityNullFieldException;
import com.lyy.secondhand.cusexception.TokenErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: ericlai
 * @Description: 全局捕获处理异常
 * @Date: 2019/3/29
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = TokenErrorException.class)
    public ResponseV0 tokenErrorException(Exception e){
        logger.error(e.getMessage());
        return new ResponseV0<String>(ResponseStrEnum.TOKEN_ERROR,"",e.getMessage());
    }

    @ExceptionHandler(value = EntityNullFieldException.class)
    public ResponseV0 handleEntityNullFieldException(Exception e){
        logger.error(e.getMessage());
        return new ResponseV0<String>(ResponseStrEnum.ENITY_FIELD_ERROR,"",e.getMessage());
    }

    @ExceptionHandler(value = CodeFromFrontErrorException.class)
    public ResponseV0 handleCodeErrorException(Exception e){
        logger.error(e.getMessage());
        return new ResponseV0<String>(ResponseStrEnum.CODE_ERROR,"",e.getMessage());
    }


}
