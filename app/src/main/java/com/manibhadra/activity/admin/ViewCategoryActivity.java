package com.manibhadra.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.adapter.CategoryAdapter;
import com.manibhadra.app.App;
import com.manibhadra.listner.DeleteCategory;
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

        adapter = new CategoryAdapter(this, categorytList, "admin", new DeleteCategory() {
            @Override
            public void deleteCategory(int position, String categoryId) {
                successDialog(ViewCategoryActivity.this,"Do you want to delete "+categorytList.get(position).categoryName+"category?",position,categoryId);
            }
        });
        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_view.setAdapter(adapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SearchView searchview = findViewById(R.id.searchview);
        searchview.clearFocus();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String newText) {
        ArrayList<CategoryInfo.CategoryListBean> tempList = new ArrayList<>();
        for (CategoryInfo.CategoryListBean categoryListBean : categorytList) {
            if (categoryListBean.categoryName.toLowerCase().contains(newText.toLowerCase())) {
                tempList.add(categoryListBean);
            }
        }
        adapter = new CategoryAdapter(this, tempList, "admin", new DeleteCategory() {
            @Override
            public void deleteCategory(int position, String categoryId) {
                successDialog(ViewCategoryActivity.this,"Do you want to delete "+categorytList.get(position).categoryName+"category?",position,categoryId);
            }
        });
        recycler_view.setAdapter(adapter);
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

    private void deleteCategoryTask(final int position, String categoryId) {
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

                    if (status.equals("success")) {
                        categorytList.remove(position);
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
                // Utils.openAlertDialog(ProductListActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
            }
        });
        service.callGetSimpleVolley("user/deleteCategory?categoryId="+categoryId+"");
    }

    public void successDialog(Context context, String message, final int position, final String categoryId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteCategoryTask(position,categoryId);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if(!activity.isFinishing())
            alert.show();
    }

}
