package com.manibhadra.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

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
        recycler_view.setAdapter(adapter);
    }
}
