package com.manibhadra.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.manibhadra.R;
import com.manibhadra.activity.admin.AddCategoryActivity;
import com.manibhadra.adapter.VendorProductAdapter;
import com.manibhadra.model.ProductInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private VendorProductAdapter adapter;
    private ArrayList<ProductInfo> vendorProductList;
    private RecyclerView recycler_view;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView iv_add_post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_add_post = findViewById(R.id.iv_add_post);
        recycler_view = findViewById(R.id.recycler_view);
        vendorProductList = new ArrayList<>();

        for(int i = 0; i< 100 ; i++){
            ProductInfo productInfo = new ProductInfo();
            productInfo.productImage = "";
            productInfo.productName = "Hexagon Head Self-drilling Screw";
            productInfo.productDescription = "Overview Warehouse, godowns, libraries, etc require ladders for easy access to heights above the normal reaching position of a person. PAffy folding ladder with 5 wide steps is one such equipment suited for such places for easy a";
            vendorProductList.add(productInfo);
        }
        adapter = new VendorProductAdapter(this,vendorProductList);
        recycler_view.setAdapter(adapter);

        iv_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddCategoryActivity.class));
            }
        });
    }

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
