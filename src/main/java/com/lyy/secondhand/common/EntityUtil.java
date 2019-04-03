package com.lyy.secondhand.common;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/29
 */
public class EntityUtil {
    //判断实体类中是否有空属性
    public static boolean isFieldNull(Object object) throws IllegalAccessException {
        Class clazz= (Class) object.getClass();//得到类对象
        Field[] fields = clazz.getDeclaredFields();//得到属性集合
        boolean flag = false;
        for (Field field: fields){
            field.setAccessible(true); //设置属性可访问，私有也可访问
            Object value = field.get(object);//得到属性值
            if (value == null){
                flag = true;
                break;
            }
        }
        return flag;
    }

    //去除首页商品项敏感数据
    public static void filterProductItem(Object object) throws IllegalAccessException{
        Set<String> filterField = new HashSet<String>()
        {{
            add("createTime");
            add("updateTime");
            add("id");
            add("openId");
            add("gender");
            add("language");
            add("province");
            add("city");
            add("country");
        }};
        Class clazz = (Class) object.getClass();
        Field[] fields = clazz.getDeclaredFields();//得到属性集合
//        for (Field field:fields){
//            System.out.println("userEntity::fields-->"+field.getName());
//        }
        for (Field field:fields){
            field.setAccessible(true);
            if (filterField.contains(field.getName())){
                field.set(object,null);
            }
        }
        Class superClazz = (Class) object.getClass().getSuperclass();
        Field[] fields1 = superClazz.getDeclaredFields();
        for (Field field1:fields1){
            field1.setAccessible(true);
            if (filterField.contains(field1.getName())){
                field1.set(object,null);
            }
        }
    }
}
