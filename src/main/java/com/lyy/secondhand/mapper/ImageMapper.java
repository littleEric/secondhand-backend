package com.lyy.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyy.secondhand.entity.ImageEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/30
 */
@Repository
public interface ImageMapper extends BaseMapper<ImageEntity> {
    Integer insertImages(List<ImageEntity> imageEntities);

    List<ImageEntity> selectByProductId(@Param("productid")Long id);
}
