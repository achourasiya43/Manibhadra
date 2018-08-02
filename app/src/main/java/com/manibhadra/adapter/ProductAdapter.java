package com.manibhadra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manibhadra.R;
import com.manibhadra.activity.admin.ProductDetailsActivity;
import com.manibhadra.model.ProductInfo;

import java.util.ArrayList;

/**
 * Created by Anil on 02-08-2018.
 */

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context mContext;
    ArrayList<ProductInfo> vendorProductList;

    public ProductAdapter(Context mContext, ArrayList<ProductInfo> vendorProductList) {
        this.mContext = mContext;
        this.vendorProductList = vendorProductList;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return vendorProductList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,ProductDetailsActivity.class);
            intent.putExtra("product_key","viewProduct");
            mContext.startActivity(intent);
        }

    }
}
