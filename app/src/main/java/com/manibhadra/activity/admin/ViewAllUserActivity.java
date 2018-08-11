package com.manibhadra.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.adapter.AllUserAdapter;
import com.manibhadra.app.App;
import com.manibhadra.model.AllUsersInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ViewAllUserActivity extends AppCompatActivity {

    private AllUserAdapter adapter;
    private ArrayList<AllUsersInfo.UsersDataBean> usertList;
    private RecyclerView recycler_view;
    private ImageView back;
    ProgressBar progress_bar;
    private SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_user);

        progress_bar = findViewById(R.id.progress_bar);
        back = findViewById(R.id.back);
        recycler_view = findViewById(R.id.recycler_view);
        usertList = new ArrayList<>();


        adapter = new AllUserAdapter(this,usertList);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchview = findViewById(R.id.searchview);
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


        viewAllUsers();
    }

    private void filter(String newText) {
        ArrayList<AllUsersInfo.UsersDataBean> tempList = new ArrayList<>();
        for (AllUsersInfo.UsersDataBean name : usertList) {
            if (name.fullName.toLowerCase().contains(newText.toLowerCase())) {
                tempList.add(name);
            }
        }
        adapter = new AllUserAdapter(this,tempList);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);
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
                    usertList.clear();
                    if (status.equals("1") || status.equals("success")) {
                        Gson gson = new Gson();
                        AllUsersInfo allUsersInfo = gson.fromJson(response,AllUsersInfo.class);
                        usertList.addAll(allUsersInfo.usersData);

                    } else {
                        Utils.openAlertDialog(ViewAllUserActivity.this, message);
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
               // Utils.openAlertDialog(ViewAllUserActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
            }
        });
        service.callGetSimpleVolley("user/getAllUsers");
    }

}
