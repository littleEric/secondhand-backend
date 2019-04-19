package com.lyy.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyy.secondhand.dto.ProductItem;
import com.lyy.secondhand.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
@Repository
public interface ProductMapper extends BaseMapper<ProductEntity> {
    Integer insertProduct(ProductEntity productEntity);

    List<ProductEntity> selectByCatBnd(@Param("category") String category, @Param("brand")String brand);

    List<ProductEntity> selectByCat(@Param("category")String category);

    List<ProductItem> selectItemById(@Param("productId") Long id,@Param("openId") String openId);

    
}
