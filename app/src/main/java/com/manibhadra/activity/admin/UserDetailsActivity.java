package com.manibhadra.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.manibhadra.R;
import com.manibhadra.model.AllUsersInfo;

public class UserDetailsActivity extends AppCompatActivity {
    AllUsersInfo.UsersDataBean usersData;
    ImageView iv_back;
    TextView tv_name,tv_email,tv_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_contact = findViewById(R.id.tv_contact);
        iv_back = findViewById(R.id.iv_back);

        if(getIntent().getSerializableExtra("userInfo") != null){
            usersData = (AllUsersInfo.UsersDataBean) getIntent().getSerializableExtra("userInfo");

            tv_name.setText(usersData.fullName);
            tv_email.setText(usersData.email);
            tv_contact.setText("0987635254");

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
