package com.lyy.secondhand.common;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: ericlai
 * @Description:
 * @Date: 2019/3/30
 */
public class CheckFieldUtil {
    private static final Set<String> category = new HashSet<String>()      //分类字段集合校验
    {{
        add("mobile");
        add("book");
        add("pad");
        add("laptop");
        add("ticket");
        add("cloth");
        add("electric");
        add("others");
    }};

    private static final Set<String> brand = new HashSet<String>()         //品牌字段集合校验
    {{
        add("apple");
        add("xiaomi");
        add("huawei");
        add("oppo");
        add("vivo");
        add("samsung");
        add("meizu");
        add("zte");
        add("oneplus");
        add("nokia");
        add("other");
        add("all");
        add("major");
        add("common");
        add("asus");
        add("google");
        add("lenovo");
        add("thinkpad");
        add("dell");
        add("asus");
        add("hp");
        add("acer");
        add("msft");
        add("hessen");
        add("movie");
        add("keepfit");
        add("ticket");
        add("men");
        add("women");
        add("jewelry");
        add("shoes");
    }};

    public CheckFieldUtil() {
    }

    public static boolean checkCategory(String cate){
        return category.contains(cate);
    }

    public static boolean checkBrand(String brnd){
        return brand.contains(brnd);
    }
}
