package com.manibhadra.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.manibhadra.R;
import com.manibhadra.model.ProductDetailsInfo;

import java.util.ArrayList;

/**
 * Created by Anil on 09-08-2018.
 */

public class DetailsProductAdapter extends RecyclerView.Adapter<DetailsProductAdapter.ViewHolder> {

    Context mContext;
    ArrayList<ProductDetailsInfo.AddProduct> productArrayList;
    String userType;

    public DetailsProductAdapter(Context mContext,
                                 ArrayList<ProductDetailsInfo.AddProduct> productArrayList,
                                 String userType) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.userType = userType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_size.setText(productArrayList.get(position).productSizes);
        holder.tv_color.setText(productArrayList.get(position).productColors);
        holder.tv_rate.setText(productArrayList.get(position).productRates);

        if(userType.equals("custmer")){
            holder.check_box.setVisibility(View.VISIBLE);
        }else {
            holder.check_box.setVisibility(View.GONE);
        }

        holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    productArrayList.get(position).isChecked = true;
                }else {
                    productArrayList.get(position).isChecked = false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_size,tv_color,tv_rate;
        CheckBox check_box;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            check_box = itemView.findViewById(R.id.check_box);
        }
    }
}
