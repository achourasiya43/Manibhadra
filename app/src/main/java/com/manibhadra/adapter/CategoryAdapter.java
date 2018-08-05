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
import com.manibhadra.activity.admin.ProductListActivity;
import com.manibhadra.model.CategoryInfo;

import java.util.ArrayList;

/**
 * Created by Anil on 02-08-2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context mContext;
    ArrayList<CategoryInfo.CategoryListBean> categoryList;
    String userType;

    public CategoryAdapter(Context mContext, ArrayList<CategoryInfo.CategoryListBean> categoryList,String userType) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.userType = userType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (categoryList.get(position).categoryImage != null)
            Glide.with(mContext).load(categoryList.get(position).categoryImage).apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into(holder.category_image);
        holder.tv_category_name.setText(categoryList.get(position).categoryName);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView category_image;
        TextView tv_category_name;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            category_image = itemView.findViewById(R.id.category_image);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,ProductListActivity.class);
            intent.putExtra("categoryId",categoryList.get(getAdapterPosition()).catId);
            intent.putExtra("userType",userType);
            mContext.startActivity(intent);

        }

    }
}
