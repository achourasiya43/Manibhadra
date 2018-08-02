package com.manibhadra.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.manibhadra.R;
import com.manibhadra.adapter.AllUserAdapter;
import com.manibhadra.adapter.ProductAdapter;
import com.manibhadra.model.AllUsersInfo;
import com.manibhadra.model.ProductInfo;

import java.util.ArrayList;


public class ViewAllUserActivity extends AppCompatActivity {

    private AllUserAdapter adapter;
    private ArrayList<AllUsersInfo> categorytList;
    private RecyclerView recycler_view;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_user);

        back = findViewById(R.id.back);
        recycler_view = findViewById(R.id.recycler_view);
        categorytList = new ArrayList<>();

        for(int i = 0; i< 100 ; i++){
            AllUsersInfo allUsersInfo = new AllUsersInfo();
            allUsersInfo.userId = "";
            allUsersInfo.userName = "User Name";
            allUsersInfo.userImage = "";
            categorytList.add(allUsersInfo);
        }
        adapter = new AllUserAdapter(this,categorytList);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
