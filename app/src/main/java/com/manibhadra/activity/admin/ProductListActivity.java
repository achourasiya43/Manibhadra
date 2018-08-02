package com.manibhadra.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.manibhadra.R;
import com.manibhadra.adapter.CategoryAdapter;
import com.manibhadra.adapter.ProductAdapter;
import com.manibhadra.model.CategoryInfo;
import com.manibhadra.model.ProductInfo;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private ArrayList<ProductInfo> categorytList;
    private RecyclerView recycler_view;
    private Button btn_add_product;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        back = findViewById(R.id.back);
        btn_add_product = findViewById(R.id.btn_add_product);
        recycler_view = findViewById(R.id.recycler_view);
        categorytList = new ArrayList<>();

        for(int i = 0; i< 100 ; i++){
            ProductInfo productInfo = new ProductInfo();
            productInfo.productId = "";
            productInfo.productName = "Product Name";
            productInfo.productImage = "";
            categorytList.add(productInfo);
        }
        adapter = new ProductAdapter(this,categorytList);
        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_view.setAdapter(adapter);


        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this,ProductDetailsActivity.class);
                intent.putExtra("product_key","addProduct");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
