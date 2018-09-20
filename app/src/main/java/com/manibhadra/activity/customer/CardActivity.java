package com.manibhadra.activity.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.activity.admin.AdminHomeActivity;
import com.manibhadra.activity.comman.SignInActivity;
import com.manibhadra.adapter.CardAdapter;
import com.manibhadra.app.App;
import com.manibhadra.helper.Constant;
import com.manibhadra.listner.Delete_Listner;
import com.manibhadra.model.ProductDetailsInfo;
import com.manibhadra.model.SignInInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;
import com.manibhadra.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private SessionManager sessionManager;
    private List<ProductDetailsInfo.ProductDetailBean> productDetailsInfo;
    private ArrayList<ProductDetailsInfo.ProductDetailBean> productDetailsArrayList;
    private CardAdapter adapter;
    private ProgressBar progress_bar;
    private Button addProductBtn;
    private int orderCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        recycler_view = findViewById(R.id.recycler_view);
        progress_bar = findViewById(R.id.progress_bar);
        addProductBtn = findViewById(R.id.addProductBtn);

        FirebaseDatabase.getInstance().getReference().child(Constant.OrderNumberTable).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Integer.class) != null){
                    orderCount = dataSnapshot.getValue(Integer.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productDetailsArrayList != null && productDetailsArrayList.size() != 0) {

                    askDialog(CardActivity.this, "Are you sure want to submit ?");
                } else {
                    Utils.openAlertDialog(CardActivity.this, "Please Add Some Product");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionManager = new SessionManager(this);
        productDetailsInfo = sessionManager.getsavecardList();
        productDetailsArrayList = new ArrayList<>();
        if (productDetailsInfo != null)
            productDetailsArrayList.addAll(productDetailsInfo);

        if (productDetailsArrayList != null) {
            adapter = new CardAdapter(productDetailsArrayList, this, new Delete_Listner() {
                @Override
                public void getDelete(int position) {
                    askDeleteDialog(CardActivity.this, "Do you want to remove?", position);

                }
            });
            recycler_view.setAdapter(adapter);
        }
    }

    private void submitCardToAdmin() {
        addProductBtn.setEnabled(false);
        progress_bar.setVisibility(View.VISIBLE);
        Gson gson = new Gson();

        final int orderNumber = orderCount+1;
        for(int i = 0;i<productDetailsArrayList.size();i++){
            productDetailsArrayList.get(i).orderNumber = orderNumber;
        }

        String json = gson.toJson(productDetailsArrayList);

        Map<String, String> map = new HashMap<>();
        map.put("orderList", json);

        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                progress_bar.setVisibility(View.GONE);
                Log.e("SIGN IN RESPONSE", response);
                addProductBtn.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {
                        FirebaseDatabase.getInstance().getReference().child(Constant.OrderNumberTable).setValue(orderNumber);
                        ArrayList<ProductDetailsInfo.ProductDetailBean> productDetailsInfo = new ArrayList<>();
                        sessionManager.savecardList(productDetailsInfo);
                    } else {

                    }
                    successDialog(CardActivity.this, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    addProductBtn.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                progress_bar.setVisibility(View.GONE);
                addProductBtn.setEnabled(true);
            }
        });
        service.callMultiPartApi("user/placeOrder", map, null);
    }

    public void successDialog(Context context, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if (!activity.isFinishing())
            alert.show();
    }

    public void askDialog(Context context, String message) {
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
        builder.setPositiveButton("Yes, Submit now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                submitCardToAdmin();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if (!activity.isFinishing())
            alert.show();
    }

    public void askDeleteDialog(Context context, String message, final int position) {
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
                dialogInterface.dismiss();
                productDetailsArrayList.remove(position);
                sessionManager.savecardList(productDetailsArrayList);
                adapter.notifyDataSetChanged();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if (!activity.isFinishing())
            alert.show();
    }
}
