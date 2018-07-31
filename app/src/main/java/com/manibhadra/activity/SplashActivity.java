package com.manibhadra.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.manibhadra.R;
import com.manibhadra.model.SignInInfo;
import com.manibhadra.session.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SessionManager session = new SessionManager(this);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SignInInfo signInInfo = session.getUser();
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        }, secondsDelayed * 3000);
    }
}
