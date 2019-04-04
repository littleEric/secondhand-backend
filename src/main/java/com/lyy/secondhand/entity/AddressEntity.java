package com.lyy.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/4
 */
@TableName(value = "address")
public class AddressEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openId;
    private Long locationId;
    private Long domAreaId;
    private String buildingNum;
    private String roomNum;
    private Integer ifDefault;
    private String name;
    private String phoneNum;

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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getDomAreaId() {
        return domAreaId;
    }

    public void setDomAreaId(Long domAreaId) {
        this.domAreaId = domAreaId;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getIfDefault() {
        return ifDefault;
    }

    public void setIfDefault(Integer ifDefault) {
        this.ifDefault = ifDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", locationId=" + locationId +
                ", domAreaId=" + domAreaId +
                ", buildingNum='" + buildingNum + '\'' +
                ", roomNum='" + roomNum + '\'' +
                ", ifDefault=" + ifDefault +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
