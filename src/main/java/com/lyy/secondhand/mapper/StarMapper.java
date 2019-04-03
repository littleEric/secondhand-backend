package com.lyy.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyy.secondhand.entity.StarEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/4/1
 */
@Repository
public interface StarMapper extends BaseMapper<StarEntity> {
    Integer selectStarByOpenIdNProductId(@Param("openId")String openId,@Param("productId") Long productId);
}
