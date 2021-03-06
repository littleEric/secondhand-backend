package com.lyy.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/5
 */
@TableName(value = "_order")
public class OrderEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String buyerOpenId;


    private Integer status;
    private Long addressId;

    @TableField(exist = false)
    private LocationEntity locationEntity;

    @TableField(exist = false)
    private DomAreaEntity domAreaEntity;


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

    public String getBuyerOpenId() {
        return buyerOpenId;
    }

    public void setBuyerOpenId(String buyerOpenId) {
        this.buyerOpenId = buyerOpenId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public LocationEntity getLocationEntity() {
        return locationEntity;
    }

    public void setLocationEntity(LocationEntity locationEntity) {
        this.locationEntity = locationEntity;
    }

    public DomAreaEntity getDomAreaEntity() {
        return domAreaEntity;
    }

    public void setDomAreaEntity(DomAreaEntity domAreaEntity) {
        this.domAreaEntity = domAreaEntity;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", productId=" + productId +
                ", buyerOpenId='" + buyerOpenId + '\'' +
                ", status=" + status +
                ", addressId=" + addressId +
                '}';
    }
}
