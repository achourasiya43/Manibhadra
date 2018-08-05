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
import com.manibhadra.activity.admin.UserDetailsActivity;
import com.manibhadra.model.AllUsersInfo;
import com.manibhadra.model.SignInInfo;

import java.util.ArrayList;

/**
 * Created by Anil on 03-08-2018.
 */

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder> {
    Context mContext;
    ArrayList<AllUsersInfo.UsersDataBean> arrayList;

    public AllUserAdapter(Context mContext, ArrayList<AllUsersInfo.UsersDataBean> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (arrayList.get(position).profileImage != null)
            Glide.with(mContext).load(arrayList.get(position).profileImage).apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into(holder.profile_image);
        holder.tv_product_name.setText(arrayList.get(position).fullName);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profile_image;
        TextView tv_product_name;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            profile_image = itemView.findViewById(R.id.profile_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, UserDetailsActivity.class);
            intent.putExtra("userInfo",arrayList.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }

    }
}
