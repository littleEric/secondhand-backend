package com.lyy.secondhand.dto;

import java.util.List;

/**
 * @Author: ericlai
 * @Description: 商品实体类
 * @Date: 2019/3/29
 */
public class Product {
    private String coverFileName;
    private Integer price;
    private String _3rd_session;
    private List<String> fileNameList;
    private String location;
    private String detail;
    private String title;
    private String category;
    private String brand;

    public void setCoverFileName(String coverFileName) {
        this.coverFileName = coverFileName;
    }

    public String getCoverFileName() {
        return coverFileName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void set_3rd_session(String _3rd_session) {
        this._3rd_session = _3rd_session;
    }
    public String get_3rd_session() {
        return _3rd_session;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }
    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getDetail() {
        return detail;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getBrand() {
        return brand;
    }

}
