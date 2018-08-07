package com.manibhadra.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.adapter.CategoryAdapter;
import com.manibhadra.adapter.ProductAdapter;
import com.manibhadra.app.App;
import com.manibhadra.listner.GetProductId;
import com.manibhadra.model.AllUsersInfo;
import com.manibhadra.model.CategoryInfo;
import com.manibhadra.model.ProductInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private ArrayList<ProductInfo.ProductListBean> productList;
    private RecyclerView recycler_view;
    private Button btn_add_product;
    private ImageView back;
    ProgressBar progress_bar;
    private String categoryId = "";
    private String userType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        progress_bar = findViewById(R.id.progress_bar);
        back = findViewById(R.id.back);
        btn_add_product = findViewById(R.id.btn_add_product);
        recycler_view = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();

        if(getIntent().getStringExtra("categoryId") != null){
            categoryId = getIntent().getStringExtra("categoryId");
            userType = getIntent().getStringExtra("userType");
        }

        if(userType.equals("custmer")){
            btn_add_product.setVisibility(View.GONE);
        }




        adapter = new ProductAdapter(this, productList, new GetProductId.getId() {
            @Override
            public void getProductId(String productId) {
                Intent intent = new Intent(ProductListActivity.this,ProductDetailsActivity.class);
                intent.putExtra("product_key","viewProduct");
                intent.putExtra("productId",productId);
                intent.putExtra("userType",userType);
                startActivity(intent);
            }
        });




        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_view.setAdapter(adapter);


        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this,ProductDetailsActivity.class);
                intent.putExtra("product_key","addProduct");
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("userType",userType);
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

    @Override
    protected void onResume() {
        super.onResume();
        viewAllUsers();
    }

    private void viewAllUsers() {
        progress_bar.setVisibility(View.VISIBLE);
        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                //loadingView.setVisibility(View.GONE);
                Log.e("SIGN IN RESPONSE", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    productList.clear();
                    if (status.equals("success")) {
                        Gson gson = new Gson();
                        ProductInfo productInfo = gson.fromJson(response,ProductInfo.class);
                        productList.addAll(productInfo.productList);

                    } else {
                        Utils.openAlertDialog(ProductListActivity.this, message);
                    }
                    progress_bar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
               // Utils.openAlertDialog(ProductListActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
            }
        });
        service.callGetSimpleVolley("user/getProductByCategory?categoryId="+categoryId+"");
    }


}
