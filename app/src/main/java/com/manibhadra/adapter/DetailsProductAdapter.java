package com.manibhadra.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.activity.admin.ProductDetailsActivity;
import com.manibhadra.listner.GetPosition;
import com.manibhadra.model.ProductDetailsInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.session.SessionManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil on 09-08-2018.
 */

public class DetailsProductAdapter extends RecyclerView.Adapter<DetailsProductAdapter.ViewHolder> {

    Context mContext;
    ArrayList<ProductDetailsInfo.AddProduct> productArrayList;
    String userType;
    GetPosition getPosition;


    public DetailsProductAdapter(Context mContext,
                                 ArrayList<ProductDetailsInfo.AddProduct> productArrayList,
                                 String userType,GetPosition getPosition) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.userType = userType;
        this.getPosition = getPosition;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_size.setText(productArrayList.get(position).productSizes);
        holder.tv_color.setText(productArrayList.get(position).productColors);
        holder.tv_rate.setText(productArrayList.get(position).productRates);
        holder.tv_qty.setText(productArrayList.get(position).productQty);

        if(userType.equals("custmer")){
            holder.check_box.setVisibility(View.VISIBLE);
            holder.ly_qyt.setVisibility(View.VISIBLE);
            holder.tv_remove.setVisibility(View.GONE);
        }
        else if(userType.equals("cardDetails")){
            holder.itemView.setEnabled(false);
            holder.ly_qyt.setVisibility(View.VISIBLE);
            holder.check_box.setVisibility(View.INVISIBLE);
            holder.tv_remove.setVisibility(View.VISIBLE);
        }
        else if(userType.equals("editCard")){
            holder.itemView.setEnabled(true);
            holder.ly_qyt.setVisibility(View.VISIBLE);
            holder.check_box.setVisibility(View.VISIBLE);
            holder.tv_remove.setVisibility(View.VISIBLE);
        }
        else if(userType.equals("upDateCard")){
            holder.itemView.setEnabled(false);
            holder.ly_qyt.setVisibility(View.VISIBLE);
            holder.check_box.setVisibility(View.INVISIBLE);
            holder.tv_remove.setVisibility(View.VISIBLE);
        }
        else if(userType.equals("admin")){
            holder.itemView.setEnabled(false);
            holder.ly_qyt.setVisibility(View.INVISIBLE);
            holder.check_box.setVisibility(View.INVISIBLE);
            holder.tv_remove.setVisibility(View.GONE);
        }
        else{
            holder.check_box.setVisibility(View.INVISIBLE);
        }

        if( productArrayList.get(position).isChecked){
            holder.check_box.setChecked(true);
        }else {
            holder.check_box.setChecked(false);
        }

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPosition.getCardItemPosition(position);            }
        });
    }

    public void getUpdate(String userType){
        this.userType = userType;
        notifyDataSetChanged();
    }

    public ArrayList<ProductDetailsInfo.AddProduct> getProductArrayList(){
        return productArrayList;
    };


    @Override
    public int getItemCount() {
        return productArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_size,tv_color,tv_rate,tv_qty,tv_remove;
        CheckBox check_box;
        LinearLayout ly_qyt;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            check_box = itemView.findViewById(R.id.check_box);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            ly_qyt = itemView.findViewById(R.id.ly_qyt);
            tv_remove = itemView.findViewById(R.id.tv_remove);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(productArrayList.get(getAdapterPosition()).isChecked){
                if(!tv_qty.getText().toString().trim().equals("")){
                    wantToClearDialog(mContext,"Do yo want to unselect",getAdapterPosition());
                }else {
                    productArrayList.get(getAdapterPosition()).isChecked = false;
                }
            }else {

                if(tv_qty.getText().toString().trim().equals("")){
                    qtyToCardDialog( productArrayList,getAdapterPosition());
                }
                productArrayList.get(getAdapterPosition()).isChecked = true;
            }

            notifyDataSetChanged();

        }

        private void qtyToCardDialog(final ArrayList<ProductDetailsInfo.AddProduct> productArrayList , final int position) {
            final Dialog dialog = new Dialog(mContext);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.add_to_card_dialog_layout);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button add_card_btn = dialog.findViewById(R.id.add_card_btn);
            final EditText ed_quentity = dialog.findViewById(R.id.ed_quentity);
            final RelativeLayout ly_notes = dialog.findViewById(R.id.ly_notes);
            ly_notes.setVisibility(View.GONE);
            ImageView close_button = dialog.findViewById(R.id.close_button);

            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productArrayList.get(position).isChecked = false;
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            add_card_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String qty = ed_quentity.getText().toString().trim();
                    if(!qty.equals("")){

                        productArrayList.get(position).isChecked = true;
                        productArrayList.get(position).productQty = qty;
                        dialog.dismiss();
                        notifyDataSetChanged();

                    }
                }
            });
            dialog.show();

        }

    }
    public void wantToClearDialog(Context context, String message, final int adapterPosition) {
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
                productArrayList.get(adapterPosition).productQty = "";
                productArrayList.get(adapterPosition).isChecked = false;
                notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if(!activity.isFinishing())
            alert.show();
    }
}
