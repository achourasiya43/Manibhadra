package com.manibhadra.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manibhadra.R;
import com.manibhadra.listner.ListnerClass;
import com.manibhadra.model.ProductDetailsInfo;
import com.manibhadra.serverTask.Utils;

import java.util.ArrayList;

/**
 * Created by Anil on 08-08-2018.
 */

public class AddProductItemAdapter  extends RecyclerView.Adapter<AddProductItemAdapter.ViewHolder>{
    ArrayList<ProductDetailsInfo.AddProduct> productsList;
    Context mContext;
    ListnerClass listnerClass;

    public AddProductItemAdapter(ArrayList<ProductDetailsInfo.AddProduct> productsList,
                                 Context mContext,
                                 ListnerClass listnerClass) {
        this.productsList = productsList;
        this.mContext = mContext;
        this.listnerClass = listnerClass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.ed_size.setText(productsList.get(position).productSizes);
        holder.ed_color.setText(productsList.get(position).productColors);
        holder.ed_rate.setText(productsList.get(position).productRates);

        holder.iv_remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wantToRemove(mContext,"Do you want to remove?",position);

            }
        });
    }


    public void wantToRemove(Context context, String message, final int pos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                productsList.remove(pos);
                notifyDataSetChanged();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if(!activity.isFinishing())
            alert.show();
    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_remove_item;
        TextView ed_size,ed_color,ed_rate;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_remove_item = itemView.findViewById(R.id.iv_remove_item);
            ed_size = itemView.findViewById(R.id.ed_size);
            ed_color = itemView.findViewById(R.id.ed_color);
            ed_rate = itemView.findViewById(R.id.ed_rate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         listnerClass.getPosition(getAdapterPosition());
        }
    }
}
