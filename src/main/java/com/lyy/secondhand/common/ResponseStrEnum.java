package com.lyy.secondhand.common;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
public enum ResponseStrEnum {
    TOKEN_ERROR("token校验出错"),
    ENITY_FIELD_ERROR("实体类有属性为空"),
    ADD_PRODUCT_SUCCESS("添加商品成功"),
    ADD_PRODUCT_FAILD("添加商品失败"),
    UPLOAD_FILE_SUCCESS("上传图片成功"),
    CODE_ERROR("Code错误"),
    STAR_SUCCESS("收藏成功"),
    UNSTAR_SUCCESS("取消收藏成功"),
    STAR_FAILED("收藏操作失败"),
    ADDRESS_ADD_SUCCESS("添加地址成功"),
    ADDRESS_ADD_FAILED("添加地址失败"),
    ADD_ORDER_SUCCESS("添加订单成功"),
    ADD_ORDER_FAILED("添加订单失败"),
    UNPUBLISHED_SUCCESS("取消发布成功"),
    UNPUBLISHED_FAILD("取消发布失败"),
    NULL_POINTER_INFO("服务器异常"),;

    private final String type;

    ResponseStrEnum(String type) {
        this.type = type;
    }

    public String getMsg() {
        return type;
    }
}
