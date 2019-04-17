package com.lyy.secondhand.common;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
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

    //判断实体类空属性,带白名单
    public static boolean isFieldNullByList(Object object, Set<String> whiteList) throws IllegalAccessException {
        Class clazz= (Class) object.getClass();//得到类对象
        Field[] fields = clazz.getDeclaredFields();//得到属性集合
        boolean flag = false;
        for (Field field: fields){
            field.setAccessible(true); //设置属性可访问，私有也可访问
            String fieldName = field.getName();
            if(whiteList.contains(fieldName)){      //如果属性名在白名单中，跳出本次循环
                break;
            }
            Object value = field.get(object);//得到属性值
            if (value == null){
                flag = true;
                break;
            }
        }
        return flag;
    }

    //过滤敏感字段，传入set白名单
    public static void fieldFilter(Object object,Set<String> whiteList) throws IllegalAccessException{
        Class clazz = (Class) object.getClass();
        Field[] fields = clazz.getDeclaredFields();     //获取属性集合
        for (Field field:fields){
            field.setAccessible(true);      //设置属性可访问
            if (whiteList.contains(field.getName())){
                field.set(object,null);
            }
        }
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

    /**
     * 设置敏感字段为null
     * @param obj
     * @param sensitiveFieldNameSet
     * @throws IllegalAccessException
     */
    public static void setSensitiveFieldNull(Object obj, Set<String> sensitiveFieldNameSet) throws IllegalAccessException{
        Class clazz = obj.getClass();
        // 获取所有属性
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field: declaredFields){
            field.setAccessible(true);
            Object value = field.get(obj);
            if (Objects.isNull(value)){
                continue;
            }
            // 不管是不是自定义类型，遍历敏感字段名单，如果相同就设为null
            for (String name: sensitiveFieldNameSet){
                if (field.getName().equals(name)){
                    field.set(obj, null);
                }
            }
            // 如果是自定义类型，递归调用本方法
            if (isSelfDefineType(value.getClass())){
                setSensitiveFieldNull(value, sensitiveFieldNameSet);
            }
        }
    }

    /**
     * 判断是否是自定义类型，非自定义类型包括: 基本类型、包装类型、String
     * @param clazz
     */
    public static boolean isSelfDefineType(Class clazz) throws IllegalAccessException{
        if (clazz.isPrimitive()){
            return false;
        } else if (clazz.getName().equals("java.lang.String")){
            return false;
        } else {
            try {
                return !((Class) clazz.getField("TYPE").get(null)).isPrimitive();
            } catch (NoSuchFieldException e){
                // 抛出NoSuchFieldException异常，说明没有TYPE这个属性，说明不是包装类
                return true;
            }
        }
    }
}
