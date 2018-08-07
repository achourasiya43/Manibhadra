package com.manibhadra.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.adapter.CategoryAdapter;
import com.manibhadra.adapter.VendorProductAdapter;
import com.manibhadra.app.App;
import com.manibhadra.model.AllUsersInfo;
import com.manibhadra.model.CategoryInfo;
import com.manibhadra.model.ProductInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCategoryActivity extends AppCompatActivity {
    private CategoryAdapter adapter;
    private ArrayList<CategoryInfo.CategoryListBean> categorytList;
    private RecyclerView recycler_view;
    private ImageView back;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        progress_bar = findViewById(R.id.progress_bar);
        back = findViewById(R.id.back);
        recycler_view = findViewById(R.id.recycler_view);
        categorytList = new ArrayList<>();

        adapter = new CategoryAdapter(this,categorytList,"admin");
        recycler_view.setAdapter(adapter);


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
        viewAllCategory();
    }

    private void viewAllCategory() {
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
                    categorytList.clear();
                    if (status.equals("success")) {
                        Gson gson = new Gson();
                        CategoryInfo categoryInfo = gson.fromJson(response,CategoryInfo.class);
                        categorytList.addAll(categoryInfo.categoryList);

                    } else {
                        Utils.openAlertDialog(ViewCategoryActivity.this, message);
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
               // Utils.openAlertDialog(ViewCategoryActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
            }
        });
        service.callGetSimpleVolley("user/getAllCategory");
    }
}
