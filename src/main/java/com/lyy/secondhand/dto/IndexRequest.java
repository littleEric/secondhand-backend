package com.lyy.secondhand.dto;

/**
 * @Author: ericlai
 * @Description: Index请求DTO
 * @Date: 2019/3/30
 */
public class IndexRequest {
    private String category;
    private String brand;
    private Integer page;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "IndexRequest{" +
                "category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", page=" + page +
                '}';
    }
}
