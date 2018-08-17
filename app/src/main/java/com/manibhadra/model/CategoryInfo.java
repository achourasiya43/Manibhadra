package com.manibhadra.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anil on 02-08-2018.
 */

public class CategoryInfo implements Serializable{
    public String status;
    public String message;
    public List<CategoryListBean> categoryList;

    public static class CategoryListBean implements Serializable{
       
        public String catId;
        public String categoryName;
        public String categoryImage;
        
    }
}
