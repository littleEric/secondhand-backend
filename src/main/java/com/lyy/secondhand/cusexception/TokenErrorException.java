package com.lyy.secondhand.cusexception;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
public class TokenErrorException extends RuntimeException{
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public TokenErrorException(String message) {
        super(message);
    }
}
