package com.lyy.secondhand.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/4
 */
@TableName(value = "dom_area")
public class DomAreaEntity {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DomAreaEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
