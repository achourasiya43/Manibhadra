package com.manibhadra.model;

/**
 * Created by Anil on 05-08-2018.
 */

public class ProductDetailsInfo {
    public String status;
    public String message;
    public ProductDetailBean productDetail;

    public static class ProductDetailBean {

        public String productId;
        public String categoryId;
        public String productName;
        public String productSize;
        public String productColorCode;
        public String productDetail;
        public String productOtherDetail;
        public String productImage;
        public String categoryName;
    }
}
