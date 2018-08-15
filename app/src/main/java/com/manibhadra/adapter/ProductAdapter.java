package com.manibhadra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.manibhadra.R;
import com.manibhadra.activity.admin.ProductDetailsActivity;
import com.manibhadra.activity.admin.ProductListActivity;
import com.manibhadra.listner.GetProductId;
import com.manibhadra.model.ProductInfo;

import java.util.ArrayList;

/**
 * Created by Anil on 02-08-2018.
 */

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context mContext;
    ArrayList<ProductInfo.ProductListBean> ProductList;
    GetProductId.getId getId;
    String userType;

    public ProductAdapter(String userType,Context mContext, ArrayList<ProductInfo.ProductListBean> ProductList,GetProductId.getId getId) {
        this.mContext = mContext;
        this.ProductList = ProductList;
        this.getId = getId;
        this.userType = userType;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(userType.equals("custmer")){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        }else view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (ProductList.get(position).productImage != null)
            Glide.with(mContext).load(ProductList.get(position).productImage).apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into( holder.product_image);


        holder.tv_product_name.setText(ProductList.get(position).productName);
    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView product_image;
        TextView tv_product_name;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            product_image = itemView.findViewById(R.id.product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
        }

        @Override
        public void onClick(View v) {
            getId.getProductId(ProductList.get(getAdapterPosition()).productId);

        }

    }
}
