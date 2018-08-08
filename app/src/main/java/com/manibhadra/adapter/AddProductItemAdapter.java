package com.manibhadra.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.manibhadra.R;
import com.manibhadra.model.ProductDetailsInfo;

import java.util.ArrayList;

/**
 * Created by Anil on 08-08-2018.
 */

public class AddProductItemAdapter  extends RecyclerView.Adapter<AddProductItemAdapter.ViewHolder>{
    ArrayList<ProductDetailsInfo.AddProduct> productsList;
    Context mContext;

    public AddProductItemAdapter(ArrayList<ProductDetailsInfo.AddProduct> productsList, Context mContext) {
        this.productsList = productsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.iv_remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_remove_item;
        EditText ed_size,ed_color,ed_rate;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_remove_item = itemView.findViewById(R.id.iv_remove_item);
            ed_size = itemView.findViewById(R.id.ed_size);
            ed_color = itemView.findViewById(R.id.ed_color);
            ed_rate = itemView.findViewById(R.id.ed_rate);
        }
    }
}
