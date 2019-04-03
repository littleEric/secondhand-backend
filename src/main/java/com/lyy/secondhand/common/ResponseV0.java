package com.lyy.secondhand.common;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
public class ResponseV0<T> {
    private Enum msg;
    private String code;
    private T data;

    public ResponseV0(Enum msg,String code,T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public Enum getMsg() {
        return msg;
    }

    public void setMsg(Enum msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
