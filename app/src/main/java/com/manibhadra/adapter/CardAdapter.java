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
import com.manibhadra.listner.Delete_Listner;
import com.manibhadra.model.ProductDetailsInfo;
import java.util.List;

/**
 * Created by Anil on 11-08-2018.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

     List<ProductDetailsInfo.ProductDetailBean> cardItemList;
    Context mContext;
    Delete_Listner listner;


    public CardAdapter(List<ProductDetailsInfo.ProductDetailBean> cardItemList, Context mContext,Delete_Listner listner) {
        this.cardItemList = cardItemList;
        this.mContext = mContext;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ProductDetailsInfo.ProductDetailBean detailBean = cardItemList.get(position);

        holder.tv_product_name.setText(detailBean.productName);
        holder.product_details.setText(detailBean.productDetail);
        if (detailBean.productImage != null)
            Glide.with(mContext).load(detailBean.productImage)
                    .apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder))
                    .into(holder.product_image);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.getDelete(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView product_image;
        ImageView iv_delete;
        TextView tv_product_name,product_details;
        public ViewHolder(View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            product_details = itemView.findViewById(R.id.product_details);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,ProductDetailsActivity.class);
            intent.putExtra("product_key","cardDetailsView");
            intent.putExtra("cardDetails",cardItemList.get(getAdapterPosition()));
            intent.putExtra("userType","");
            mContext.startActivity(intent);
        }
    }
}
