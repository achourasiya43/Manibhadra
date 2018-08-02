package com.manibhadra.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manibhadra.R;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView back;
    String product_key;
    TextView action_bar_title;
    Button addProductBtn;
    LinearLayout details_view_layout,add_view_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        add_view_layout = findViewById(R.id.add_view_layout);
        details_view_layout = findViewById(R.id.details_view_layout);

        addProductBtn = findViewById(R.id.addProductBtn);
        back = findViewById(R.id.back);
        action_bar_title = findViewById(R.id.action_bar_title);

        if(getIntent().getStringExtra("product_key") != null){
            product_key = getIntent().getStringExtra("product_key");
            if(product_key.equals("viewProduct")){
                action_bar_title.setText("Product Details");
                addProductBtn.setText("Delete Product");

                add_view_layout.setVisibility(View.GONE);
                details_view_layout.setVisibility(View.VISIBLE);
            }
            else if(product_key.equals("addProduct")){
                action_bar_title.setText("Add Product");

                add_view_layout.setVisibility(View.VISIBLE);
                details_view_layout.setVisibility(View.GONE);

            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
