package com.manibhadra.model;

import java.util.List;

/**
 * Created by Anil on 02-08-2018.
 */

public class CategoryInfo {
    public String status;
    public String message;
    public List<CategoryListBean> categoryList;

    public static class CategoryListBean {
       
        public String catId;
        public String categoryName;
        public String categoryImage;
        
    }
}
