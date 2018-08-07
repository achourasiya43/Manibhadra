package com.manibhadra.activity.comman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.manibhadra.R;
import com.manibhadra.app.App;
import com.manibhadra.helper.Validation;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassActivity extends AppCompatActivity {
    private TextView create_signIn;
    ProgressBar progress_bar;
    Button submitBtn;
    EditText ed_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        create_signIn = findViewById(R.id.create_signIn);
        progress_bar = findViewById(R.id.progress_bar);
        submitBtn = findViewById(R.id.submitBtn);
        ed_email = findViewById(R.id.ed_email);

        create_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidData()){
                    forgotPassword();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPassActivity.this, SignInActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    private void forgotPassword() {
        submitBtn.setEnabled(false);
        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("email", ed_email.getText().toString().trim());

        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                Log.e("SIGN IN RESPONSE", response);
                submitBtn.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {

                        successDialog(ForgotPassActivity.this,message);
                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Utils.openAlertDialog(ForgotPassActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    submitBtn.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                progress_bar.setVisibility(View.GONE);
                submitBtn.setEnabled(true);
            }
        });
        service.callMultiPartApi("forgotPassword", map, null);

    }

    public  void successDialog(Context context, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(ForgotPassActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if(!activity.isFinishing())
            alert.show();
    }

    public boolean isValidData() {
        Validation v = new Validation();

        if (!v.isNullValue(ed_email.getText().toString().trim())) {
            Utils.openAlertDialog(ForgotPassActivity.this, getResources().getString(R.string.alert_email_null));

            return false;
        } else if (!v.isEmailValid(ed_email.getText().toString().trim())) {
            Utils.openAlertDialog(ForgotPassActivity.this, getResources().getString(R.string.alert_invalid_email));
            return false;
        }

        return true;
    }
}
