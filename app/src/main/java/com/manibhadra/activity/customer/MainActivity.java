package com.manibhadra.activity.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.activity.admin.AddCategoryActivity;
import com.manibhadra.activity.admin.AdminHomeActivity;
import com.manibhadra.activity.admin.ProductDetailsActivity;
import com.manibhadra.activity.admin.ProductListActivity;
import com.manibhadra.activity.comman.ChangePasswordActivity;
import com.manibhadra.adapter.CategoryAdapter;
import com.manibhadra.adapter.ProductAdapter;
import com.manibhadra.app.App;
import com.manibhadra.listner.GetProductId;
import com.manibhadra.model.CategoryInfo;
import com.manibhadra.model.ProductDetailsInfo;
import com.manibhadra.model.ProductInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;
import com.manibhadra.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CategoryAdapter adapter;
    private ArrayList<CategoryInfo.CategoryListBean> categorytList;
    private RecyclerView recycler_view;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView iv_add_card;
    private ImageView iv_settings;
    ProgressBar progress_bar;
    SessionManager sessionManager;
    List<ProductDetailsInfo.ProductDetailBean> productDetailsInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress_bar = findViewById(R.id.progress_bar);
        iv_settings = findViewById(R.id.iv_settings);
        iv_add_card = findViewById(R.id.iv_add_card);
        recycler_view = findViewById(R.id.recycler_view);

        sessionManager = new SessionManager(this);
        categorytList = new ArrayList<>();

        productDetailsInfo = sessionManager.getsavecardList();

        adapter = new CategoryAdapter(this, categorytList, "custmer");
        recycler_view.setAdapter(adapter);

        iv_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CardActivity.class));
            }
        });

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCardDialog();
            }
        });

        SearchView searchview = findViewById(R.id.searchview);
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
        adapter = new CategoryAdapter(this, tempList, "custmer");
        recycler_view.setAdapter(adapter);
    }

    public void logoutDialog(Context context, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sessionManager.logout();
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if (!activity.isFinishing())
            alert.show();
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
                        CategoryInfo categoryInfo = gson.fromJson(response, CategoryInfo.class);
                        categorytList.addAll(categoryInfo.categoryList);

                    } else {
                        Utils.openAlertDialog(MainActivity.this, message);
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
                // Utils.openAlertDialog(MainActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
            }
        });
        service.callGetSimpleVolley("user/getAllCategory");
    }


    private void addToCardDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.settings_dialog_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tv_change_pass = dialog.findViewById(R.id.tv_change_pass);
        TextView tv_logout = dialog.findViewById(R.id.tv_logout);

        ImageView close_button = dialog.findViewById(R.id.close_button);

        tv_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logoutDialog(MainActivity.this, "Do you want to logout.?");
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
