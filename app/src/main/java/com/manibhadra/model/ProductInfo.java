package com.manibhadra.model;

import java.util.List;

/**
 * Created by Anil on 31-07-2018.
 */

public class ProductInfo {
    public String status;
    public String message;
    public List<ProductListBean> productList;
    
    public static class ProductListBean {

        public String productId;
        public String categoryId;
        public String productName;
        public String productSize;
        public String productColorCode;
        public String productDetail;
        public String productOtherDetail;
        public String productImage;
    }
}
