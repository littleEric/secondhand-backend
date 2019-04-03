package com.lyy.secondhand.cusexception;

/**
 * @Author: ericlai
 * @Description: wx.login返回的code非小程序前端生成，非法访问
 * @Date: 2019/3/30
 */
public class CodeFromFrontErrorException extends RuntimeException {
    public CodeFromFrontErrorException(String message) {
        super(message);
    }
}
