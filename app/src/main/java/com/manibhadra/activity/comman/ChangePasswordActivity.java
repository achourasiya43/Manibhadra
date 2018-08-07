package com.manibhadra.activity.comman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.manibhadra.R;
import com.manibhadra.app.App;
import com.manibhadra.helper.Validation;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;
import com.manibhadra.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    ImageView back;
    EditText ed_old_password,ed_new_password;
    Button submitBtn;
    SessionManager sessionManager;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        back = findViewById(R.id.back);
        ed_old_password = findViewById(R.id.ed_old_password);
        ed_new_password = findViewById(R.id.ed_new_password);
        submitBtn = findViewById(R.id.submitBtn);
        progress_bar = findViewById(R.id.progress_bar);

        sessionManager = new SessionManager(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData()){
                    changePassword();
                }
            }
        });
    }

    private void changePassword() {
        submitBtn.setEnabled(false);
        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("oldPassword", ed_old_password.getText().toString().trim());
        map.put("newPassword", ed_new_password.getText().toString().trim());

        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                //loadingView.setVisibility(View.GONE);
                Log.e("SIGN IN RESPONSE", response);
                submitBtn.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {
                        sessionManager.savePassword(ed_new_password.getText().toString().trim());
                        successDialog(ChangePasswordActivity.this,message);

                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Utils.openAlertDialog(ChangePasswordActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    submitBtn.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
               // Utils.openAlertDialog(ChangePasswordActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
                submitBtn.setEnabled(true);
            }
        });
        service.callMultiPartApi("user/changePassword", map,null);
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
        if(!activity.isFinishing())
            alert.show();
    }

    public boolean isValidData() {
        String savedPassword = sessionManager.getPassword();

        String oldPassword = ed_old_password.getText().toString().trim();
        String newPassword = ed_new_password.getText().toString().trim();


        Validation v = new Validation();

        if (!v.isNullValue(oldPassword)) {
            Utils.openAlertDialog(ChangePasswordActivity.this, "Please enter old password");

            return false;
        }
        else if (!savedPassword.equals(oldPassword)) {
            Utils.openAlertDialog(ChangePasswordActivity.this, "Please enter correct old password");
            return false;
        }
        else if (!v.isNullValue(newPassword)) {
            Utils.openAlertDialog(ChangePasswordActivity.this, "Please enter new password");
            return false;
        }
        else if (!v.isPasswordValid(newPassword)) {
            Utils.openAlertDialog(ChangePasswordActivity.this, getResources().getString(R.string.alert_invalid_password));
            return false;
        }

        return true;
    }
}
