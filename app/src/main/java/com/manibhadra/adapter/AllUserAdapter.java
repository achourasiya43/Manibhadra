package com.manibhadra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manibhadra.R;
import com.manibhadra.activity.admin.UserDetailsActivity;
import com.manibhadra.model.AllUsersInfo;

import java.util.ArrayList;

/**
 * Created by Anil on 03-08-2018.
 */

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder> {
    Context mContext;
    ArrayList<AllUsersInfo> arrayList;

    public AllUserAdapter(Context mContext, ArrayList<AllUsersInfo> arrayList) {
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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,UserDetailsActivity.class);
            mContext.startActivity(intent);
        }

    }
}
