package com.lyy.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/1
 */

@TableName("star")
public class StarEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openId;
    private Long productId;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StarEntity{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", productId=" + productId +
                ", status=" + status +
                '}';
    }
}
