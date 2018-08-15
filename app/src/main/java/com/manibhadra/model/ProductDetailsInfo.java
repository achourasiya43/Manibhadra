package com.manibhadra.model;

import java.io.Serializable;

/**
 * Created by Anil on 05-08-2018.
 */

public class ProductDetailsInfo implements Serializable{
    public String status;
    public String message;
    public ProductDetailBean productDetail;
    public AddProduct addProduct;

    public static class ProductDetailBean implements Serializable{

        public String productId;
        public String categoryId;
        public String productName;
        public String productDetail;
        public String productOtherDetail;
        public String productImage;
        public String categoryName;
        public String productData;
        public String note;

    }

    public static class AddProduct implements Serializable{
        public String productSizes;
        public String productColors;
        public String productRates;
        public String productQty;
        public boolean isChecked;
    }
}
