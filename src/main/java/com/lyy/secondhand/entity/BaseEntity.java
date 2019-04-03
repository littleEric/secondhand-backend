package com.lyy.secondhand.entity;

import com.lyy.secondhand.aop.CreateTime;
import com.lyy.secondhand.aop.UpdateTime;

import java.sql.Timestamp;

public class BaseEntity {


    @CreateTime
    private Timestamp createTime;

    @UpdateTime
    private Timestamp updateTime;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


}
