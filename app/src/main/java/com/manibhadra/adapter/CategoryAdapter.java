package com.manibhadra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.activity.admin.AddCategoryActivity;
import com.manibhadra.activity.admin.ProductListActivity;
import com.manibhadra.app.App;
import com.manibhadra.helper.Constant;
import com.manibhadra.listner.DeleteCategory;
import com.manibhadra.model.CategoryInfo;
import com.manibhadra.model.ProductInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;
import com.manibhadra.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Anil on 02-08-2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context mContext;
    ArrayList<CategoryInfo.CategoryListBean> categoryList;
    String userType;
    DeleteCategory deleteCategory;


    public CategoryAdapter(Context mContext, ArrayList<CategoryInfo.CategoryListBean> categoryList,
                           String userType, DeleteCategory deleteCategory) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.userType = userType;
        this.deleteCategory = deleteCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(Constant.isYouAreAdmin){
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_edit.setVisibility(View.VISIBLE);
        }else {
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_edit.setVisibility(View.GONE);
        }



        if (categoryList.get(position).categoryImage != null)
            Glide.with(mContext).load(categoryList.get(position).categoryImage).apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into(holder.category_image);
        holder.tv_category_name.setText(categoryList.get(position).categoryName);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory.deleteCategory(position, categoryList.get(position).catId);
            }
        });

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, AddCategoryActivity.class).putExtra("categoryInfo",categoryList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView category_image, iv_delete,iv_edit;
        TextView tv_category_name;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            category_image = itemView.findViewById(R.id.product_image);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            tv_category_name = itemView.findViewById(R.id.tv_product_name);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ProductListActivity.class);
            intent.putExtra("categoryId", categoryList.get(getAdapterPosition()).catId);
            intent.putExtra("userType", userType);
            mContext.startActivity(intent);

        }

    }


}
