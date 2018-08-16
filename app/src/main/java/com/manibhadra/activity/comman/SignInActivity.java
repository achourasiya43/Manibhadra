package com.manibhadra.activity.comman;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.manibhadra.R;
import com.manibhadra.activity.admin.AdminHomeActivity;
import com.manibhadra.activity.customer.MainActivity;
import com.manibhadra.app.App;
import com.manibhadra.helper.Constant;
import com.manibhadra.helper.Validation;
import com.manibhadra.model.SignInInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;
import com.manibhadra.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    private TextView create_signIn;
    private TextView forgot_password;
    private Button signInBtn;
    EditText sign_in_email, sign_in_password;
    SessionManager session;
    ProgressBar progress_bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        create_signIn = findViewById(R.id.create_signIn);
        signInBtn = findViewById(R.id.signInBtn);
        forgot_password = findViewById(R.id.forgot_password);
        progress_bar = findViewById(R.id.progress_bar);
        sign_in_email = findViewById(R.id.sign_in_email);
        sign_in_password = findViewById(R.id.sign_in_password);

        session = new SessionManager(this);

        create_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData()){
                    signInTask();
                }


            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPassActivity.class));
                finish();

            }
        });
    }

    private void signInTask() {
        create_signIn.setEnabled(false);
        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("email", sign_in_email.getText().toString().trim());
        map.put("password", sign_in_password.getText().toString().trim());
        map.put("deviceToken", "you will get soon");
        map.put("deviceType", "1");

        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                //loadingView.setVisibility(View.GONE);
                Log.e("SIGN IN RESPONSE", response);
                create_signIn.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {
                        Gson gson = new Gson();
                        SignInInfo signInInfo = gson.fromJson(String.valueOf(jsonObject), SignInInfo.class);
                        session.createSession(signInInfo);

                        if(session.getUser().userDetail.email.equals(Constant.AdminEmail)){
                            Constant.isYouAreAdmin = true;
                        }else Constant.isYouAreAdmin = false;

                        session.savePassword(sign_in_password.getText().toString().trim());
                        if(signInInfo.userDetail.email.equals(Constant.AdminEmail)){
                            startActivity(new Intent(SignInActivity.this, AdminHomeActivity.class));
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAfterTransition();
                            }
                        }else {
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAfterTransition();
                            }
                        }


                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Utils.openAlertDialog(SignInActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    create_signIn.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                Utils.openAlertDialog(SignInActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
                create_signIn.setEnabled(true);
            }
        });
        service.callMultiPartApi("userLogin", map,null);
    }

    public boolean isValidData() {
        Validation v = new Validation();

        if (!v.isNullValue(sign_in_email.getText().toString().trim())) {
            Utils.openAlertDialog(SignInActivity.this, getResources().getString(R.string.alert_email_null));

            return false;
        } else if (!v.isEmailValid(sign_in_email.getText().toString().trim())) {
            Utils.openAlertDialog(SignInActivity.this, getResources().getString(R.string.alert_invalid_email));
            return false;
        } else if (!v.isNullValue(sign_in_password.getText().toString().trim())) {
            Utils.openAlertDialog(SignInActivity.this, getResources().getString(R.string.alert_password_null));

            return false;
        } else if (!v.isPasswordValid(sign_in_password.getText().toString().trim())) {
            Utils.openAlertDialog(SignInActivity.this, getResources().getString(R.string.alert_invalid_password));
            return false;
        }

        return true;
    }

}
