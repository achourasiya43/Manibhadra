package com.manibhadra.activity.admin;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.manibhadra.R;

public class AdminHomeActivity extends AppCompatActivity {
    private CardView cv_view_all_user,cv_add_category,cv_view_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        cv_view_all_user = findViewById(R.id.cv_view_all_user);
        cv_add_category = findViewById(R.id.cv_add_category);
        cv_view_category = findViewById(R.id.cv_view_category);

        cv_view_all_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this,ViewAllUserActivity.class);
                startActivity(intent);
            }
        });

        cv_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this,AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        cv_view_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this,ViewCategoryActivity.class);
                startActivity(intent);
            }
        });


    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
