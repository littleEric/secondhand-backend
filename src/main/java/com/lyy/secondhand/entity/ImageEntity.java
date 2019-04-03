package com.lyy.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/30
 */
@TableName("img")
public class ImageEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String imgUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "id=" + id +
                ", productId=" + productId +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
