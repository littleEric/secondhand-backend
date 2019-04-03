package com.lyy.secondhand.cusexception;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
public class EntityNullFieldException extends RuntimeException {
    public EntityNullFieldException(String message) {
        super(message);
    }
}
