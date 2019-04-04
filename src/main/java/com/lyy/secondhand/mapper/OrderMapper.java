package com.lyy.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyy.secondhand.entity.OrderEntity;
import org.springframework.stereotype.Repository;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/5
 */
@Repository
public interface OrderMapper extends BaseMapper<OrderEntity> {
    Integer insertOrder(OrderEntity orderEntity);
}
