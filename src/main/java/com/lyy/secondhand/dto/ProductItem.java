package com.lyy.secondhand.dto;

import com.lyy.secondhand.entity.ImageEntity;

import java.util.List;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/1
 */
public class ProductItem {
    private String avatarUrl;
    private String name;
    private Long productId;
    private String title;
    private Integer location;
    private Integer price;
    private String detail;
    private String coverUrl;
    private List<ImageEntity> imgUrlList;
    private Integer ifStar;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<ImageEntity> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<ImageEntity> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public Integer getIfStar() {
        return ifStar;
    }

    public void setIfStar(Integer ifStar) {
        this.ifStar = ifStar;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", name='" + name + '\'' +
                ", productId=" + productId +
                ", title='" + title + '\'' +
                ", location=" + location +
                ", price=" + price +
                ", detail='" + detail + '\'' +
                ", imgUrlList=" + imgUrlList +
                ", ifStar=" + ifStar +
                '}';
    }
}
