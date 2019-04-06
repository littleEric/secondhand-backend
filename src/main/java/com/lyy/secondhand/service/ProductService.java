package com.lyy.secondhand.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lyy.secondhand.common.*;
import com.lyy.secondhand.cusexception.EntityNullFieldException;
import com.lyy.secondhand.dto.Product;
import com.lyy.secondhand.dto.ProductItem;
import com.lyy.secondhand.entity.*;
import com.lyy.secondhand.mapper.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final String MIME_JPEG = "image/jpeg";
    private final String MIME_JPG = "image/jpg";
    private final String MIME_PNG = "image/png";
    private final String STATUS_SUCCEED = "SAVE SUCCEED";
    private final String STATUS_FAILED = "SAVE FAILED";
    private final String STATUS_OUT_OF_SIZE = "OUT OF SIZE";
    private final String STATUS_SUCCEED_CODE = "100200";
    private final String STATUS_FAILED_CODE = "100400";
    private final String STATUS_OUT_OF_SIZE_CODE = "100410";

    //文件保存路径,从配置文件注入
    @Value("${uploadpic.path}")
    private String fileFloderName;

    //Redis操作类
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ImageMapper imageMapper;

    @Autowired
    StarMapper starMapper;

    @Autowired
    OrderMapper orderMapper;

    //只接受jpge和jpg文件
    private final Set<String> fileNameSet = new HashSet<>(Arrays.asList(MIME_JPEG,MIME_JPG,MIME_PNG));

    public String saveFile(MultipartFile multipartFile){
        String status = STATUS_FAILED;
        String code = STATUS_FAILED_CODE;
        String fileName = "";

        System.out.println("PublishService::saveFile--->"+multipartFile.getContentType());

        //文件后缀
        String suffix = "";
        //图片大小超过10M
        if (multipartFile.getSize() > 10*1024*1024){
            status = STATUS_OUT_OF_SIZE;
            code = STATUS_OUT_OF_SIZE_CODE;
        }else if(fileNameSet.contains(multipartFile.getContentType())){
            switch (multipartFile.getContentType()){
                case MIME_JPEG:
                    suffix = ".jpeg";
                    break;
                case MIME_JPG:
                    suffix = ".jpg";
                    break;
                case MIME_PNG:
                    suffix = ".png";
                    break;
            }

            //文件名
            fileName = UUID.randomUUID().toString().replaceAll("-","") + suffix;
            String filePath = FilenameUtils.separatorsToSystem(fileFloderName + "/" + fileName);
            //保存文件
            File dest = new File(filePath);
            try {
                multipartFile.transferTo(dest);
            }catch (IOException e){
                e.printStackTrace();
            }
            status = STATUS_SUCCEED;
            code = STATUS_SUCCEED_CODE;
        }
        JSONObject result = new JSONObject();
        result.put("code",code);
        result.put("status",status);
        result.put("fileName",fileName);
        logger.info(JSON.toJSONString(result));
        return result.toJSONString();
    }


    //添加商品
    public ResponseV0<String> addProduct(Product product){
        try {
            //判断参数非空
            if (EntityUtil.isFieldNull(product)){
                throw new EntityNullFieldException("实体类中有空属性");
            }
        }catch (IllegalAccessException e){
            logger.error(e.getMessage());
        }

        String open_id = redisUtil.get(product.get_3rd_session());
        UserEntity user = userMapper.selectByOpenId(open_id);

        //实例化商品实体类
        ProductEntity productEntity = new ProductEntity();
        productEntity.setBrand(product.getBrand());
        productEntity.setCategory(product.getCategory());
        productEntity.setCoverFileName(product.getCoverFileName());
        productEntity.setDetail(product.getDetail());
        productEntity.setLocation(product.getLocation());
        productEntity.setPrice(product.getPrice());
        productEntity.setUserEntity(user);
        productEntity.setStatus(new Byte("0"));
        productEntity.setTitle(product.getTitle());
//        System.out.println("productMapper::insetProduct->"+productMapper.insertProduct(productEntity));
        if (productMapper.insertProduct(productEntity) == 1){      //插入商品表
            System.out.println(productEntity.getId());
            Long product_id = new Long(productEntity.getId());      //查出商品id
            List<ImageEntity> imgList = new ArrayList<>();
            for (String fileUrl:product.getFileNameList()){
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setProductId(product_id);
                imageEntity.setImgUrl(fileUrl);
                imgList.add(imageEntity);
            }
            if (imageMapper.insertImages(imgList) > 0){            //插入图片表
                return new ResponseV0<>(ResponseStrEnum.ADD_PRODUCT_SUCCESS,"",ResponseStrEnum.ADD_PRODUCT_SUCCESS.getMsg());   //返回成功请求
            }
            logger.error("PublishService::AddProduct Error:{}","插入商品图片失败");
        }
        logger.error("PublishService::AddProduct Error:{}","插入商品失败");
        return new ResponseV0<>(ResponseStrEnum.ADD_PRODUCT_FAILD,"",ResponseStrEnum.ADD_PRODUCT_FAILD.getMsg());   //返回插入失败请求
    }

    //根据商品id获取item信息
    public ProductItem selectItem(Long id){
        List<ProductItem> result = productMapper.selectItemById(id);
        if (!(result.size() == 1)){
            logger.error("ProductService::selectItem::result.size ----->> !== 1");
            return null;
        }
        return result.get(0);
    }

    //更新商品收藏状态
    public ResponseV0<String> updateStar(String token,Long productId,Integer action){
        //通过token获取openid
        String openId = redisUtil.get(token);
        //首次更新收藏，执行插入操作
        //action为1时收藏，0取消收藏
        int count = starMapper.selectCount(new QueryWrapper<StarEntity>().eq("open_id",openId).eq("product_id",productId));
        if (count == 0){
            if (action == 1){
                StarEntity starEntity = new StarEntity();
                starEntity.setOpenId(openId);
                starEntity.setProductId(productId);
                starEntity.setStatus(1);
                Integer ins = starMapper.insert(starEntity);
                return new ResponseV0<String>(ResponseStrEnum.STAR_SUCCESS,"",ResponseStrEnum.STAR_SUCCESS.getMsg());
            }
        }else {
            if (action == 1){
                StarEntity starEntity = new StarEntity();
                starEntity.setStatus(1);
                Integer ins = starMapper.update(starEntity,new UpdateWrapper<StarEntity>().eq("open_id",openId).eq("product_id",productId));
                return new ResponseV0<String>(ResponseStrEnum.STAR_SUCCESS,"",ResponseStrEnum.STAR_SUCCESS.getMsg());
            }
            if (action == 0){
                StarEntity starEntity = new StarEntity();
                starEntity.setStatus(0);
                Integer ins = starMapper.update(starEntity,new UpdateWrapper<StarEntity>().eq("open_id",openId).eq("product_id",productId));
                return new ResponseV0<String>(ResponseStrEnum.UNSTAR_SUCCESS,"",ResponseStrEnum.UNSTAR_SUCCESS.getMsg());
            }
        }
        return new ResponseV0<>(ResponseStrEnum.STAR_FAILED,"",ResponseStrEnum.STAR_FAILED.getMsg());

    }

    //获取用户已发布商品列表
    public List<ProductEntity> getPublishedList(String token){
        String openId = redisUtil.get(token);
        if (!openId.equals("")){
            List<ProductEntity> productEntities = productMapper.selectList(new QueryWrapper<ProductEntity>().eq("open_id",openId).eq("status",0));
            Set<String> whiteList = new HashSet<String>(){{
                add("openId");
            }};
            try{
                for (ProductEntity productEntity:productEntities){
                    EntityUtil.fieldFilter(productEntity,whiteList);
                }
            }catch (IllegalAccessException e){
                logger.error("ProductService::getPublisedList-->{}",e.getMessage());
            }
            return productEntities;
        }
        return null;
    }

    //获取用户已购买的商品列表
    public List<ProductEntity> getBoughtList(String token){
        String openId = redisUtil.get(token);
        if (!openId.equals("")){
            List<OrderEntity> orderEntities = orderMapper.selectList(new QueryWrapper<OrderEntity>().eq("buyer_open_id",openId).eq("status",1).select("product_id"));
            List<Long> productIds= orderEntities.stream().map(OrderEntity::getProductId).collect(Collectors.toList());
            List<ProductEntity> productEntities = productMapper.selectBatchIds(productIds);
            //敏感字段设置为null
            Set<String> whiteList = new HashSet<String>(){{
                add("openId");
            }};
            try{
                for (ProductEntity productEntity:productEntities){
                    EntityUtil.fieldFilter(productEntity,whiteList);
                }
            }catch (IllegalAccessException e){
                logger.error("ProductService::getBoughtList-->{}",e.getMessage());
            }
            return productEntities;
        }
        return null;
    }

    //获取用户卖出的商品列表
    public List<ProductEntity> getSoldList(String token){
        String openId = redisUtil.get(token);
        if (!openId.equals("")){
            List<ProductEntity> productEntities = productMapper.selectList(new QueryWrapper<ProductEntity>().eq("open_id",openId).eq("status",1));
            //敏感字段设置为null
            Set<String> whiteList = new HashSet<String>(){{
                add("openId");
            }};
            try{
                for (ProductEntity productEntity:productEntities){
                    EntityUtil.fieldFilter(productEntity,whiteList);
                }
            }catch (IllegalAccessException e){
                logger.error("ProductService::getBoughtList-->{}",e.getMessage());
            }
            return productEntities;
        }
        return null;
    }

    //获取用户收藏的商品列表
    public List<ProductEntity> getStarList(String token){
        String openId = redisUtil.get(token);
        if (!openId.equals("")){
            List<StarEntity> starEntities = starMapper.selectList(new QueryWrapper<StarEntity>().eq("open_id",openId).eq("status",1).select("product_id"));
            List<Long> productIdList = starEntities.stream().map(StarEntity::getProductId).collect(Collectors.toList());
            List<ProductEntity> productEntities = productMapper.selectBatchIds(productIdList).stream().filter(productEntity -> productEntity.getStatus() == 0).collect(Collectors.toList());
            //敏感字段设置为null
            Set<String> whiteList = new HashSet<String>(){{
                add("openId");
            }};
            try{
                for (ProductEntity productEntity:productEntities){
                    EntityUtil.fieldFilter(productEntity,whiteList);
                }
            }catch (IllegalAccessException e){
                logger.error("ProductService::getBoughtList-->{}",e.getMessage());
            }
            return productEntities;
        }
        return null;

    }

    //取消发布
    public ResponseV0<String> unPublish(Long productId){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setStatus(new Byte("3"));
        int result = productMapper.updateById(productEntity);
        if (result > 1){
            logger.error("ProductService::unPublish-->{}","update > 1");
            return new ResponseV0<>(ResponseStrEnum.UNPUBLISHED_FAILD,"",ResponseStrEnum.UNPUBLISHED_FAILD.getMsg());
        }
        if (result == 1){
            return new ResponseV0<>(ResponseStrEnum.UNPUBLISHED_SUCCESS,"",ResponseStrEnum.UNPUBLISHED_SUCCESS.getMsg());
        }
        return new ResponseV0<>(ResponseStrEnum.UNPUBLISHED_FAILD,"",ResponseStrEnum.UNPUBLISHED_FAILD.getMsg());
    }

}
