package com.manibhadra.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.manibhadra.R;
import com.manibhadra.adapter.CategoryAdapter;
import com.manibhadra.adapter.VendorProductAdapter;
import com.manibhadra.model.CategoryInfo;
import com.manibhadra.model.ProductInfo;

import java.util.ArrayList;

public class ViewCategoryActivity extends AppCompatActivity {
    private CategoryAdapter adapter;
    private ArrayList<CategoryInfo> categorytList;
    private RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        recycler_view = findViewById(R.id.recycler_view);
        categorytList = new ArrayList<>();

        for(int i = 0; i< 100 ; i++){
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.CategoryId = "";
            categoryInfo.CategoryInfoName = "Category Name";
            categoryInfo.CategoryInfoImage = "";
            categorytList.add(categoryInfo);
        }
        adapter = new CategoryAdapter(this,categorytList);
        recycler_view.setAdapter(adapter);
    }
}
