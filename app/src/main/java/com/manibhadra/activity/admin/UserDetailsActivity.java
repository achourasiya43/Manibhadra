package com.manibhadra.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manibhadra.R;
import com.manibhadra.helper.Constant;
import com.manibhadra.model.AllUsersInfo;
import com.manibhadra.serverTask.Utils;

public class UserDetailsActivity extends AppCompatActivity {
    AllUsersInfo.UsersDataBean usersData;
    ImageView iv_back,user_img;
    TextView tv_name,tv_email,tv_contact,tv_isBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_contact = findViewById(R.id.tv_contact);
        iv_back = findViewById(R.id.iv_back);
        user_img = findViewById(R.id.user_img);
        tv_isBlock = findViewById(R.id.tv_isBlock);

        if(getIntent().getSerializableExtra("userInfo") != null){
            usersData = (AllUsersInfo.UsersDataBean) getIntent().getSerializableExtra("userInfo");
            tv_name.setText(usersData.fullName);
            tv_email.setText(usersData.email);
            tv_contact.setText(usersData.contactNumber);
            Glide.with(this).load(usersData.profileImage).apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into(user_img);
        }
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usersData.profileImage.equals("")){
                    Utils.full_screen_photo_dialog(UserDetailsActivity.this,usersData.profileImage);
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseDatabase.getInstance().getReference().child(Constant.BlockTable).child(usersData.userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(AllUsersInfo.UsersDataBean.class) != null){
                    AllUsersInfo.UsersDataBean bean = dataSnapshot.getValue(AllUsersInfo.UsersDataBean.class);
                    tv_isBlock.setText("Unblock");
                }else {
                    tv_isBlock.setText("Block");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        tv_isBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_isBlock.getText().toString().trim().equals("Block")){
                    isBlockDialog(UserDetailsActivity.this,"Do you want to block "+usersData.fullName+" ?");
                }else {
                    isBlockDialog(UserDetailsActivity.this,"Do you want to unblock "+usersData.fullName+" ?");

                }
            }
        });
    }

    public  void isBlockDialog(Context context, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(tv_isBlock.getText().toString().trim().equals("Block")){
                    FirebaseDatabase.getInstance().getReference().child(Constant.BlockTable).child(usersData.userId).setValue(usersData);
                }else {
                    FirebaseDatabase.getInstance().getReference().child(Constant.BlockTable).child(usersData.userId).setValue(null);

                }
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if(!activity.isFinishing())
            alert.show();
    }
}
