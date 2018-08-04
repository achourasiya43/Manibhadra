package com.manibhadra.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.manibhadra.ImagePickerPackge.ImagePicker;
import com.manibhadra.R;
import com.manibhadra.activity.admin.AdminHomeActivity;
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

public class SignUpActivity extends AppCompatActivity {
    private TextView create_signIn;
    SessionManager session;
    ProgressBar progress_bar;
    EditText sign_in_email, sign_in_contact, sign_in_password, sign_in_name;
    Bitmap bitmap;
    ImageView profile_image;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        create_signIn = findViewById(R.id.create_signIn);
        progress_bar = findViewById(R.id.progress_bar);
        sign_in_name = findViewById(R.id.sign_in_name);
        sign_in_email = findViewById(R.id.sign_in_email);
        sign_in_contact = findViewById(R.id.sign_in_contact);
        sign_in_password = findViewById(R.id.sign_in_password);
        profile_image = findViewById(R.id.profile_image);
        signUpBtn = findViewById(R.id.signUpBtn);

        session = new SessionManager(this);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionAndPicImage();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidData()) {
                    signUpTask();
                }
            }
        });

        create_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        });
    }


    public void getPermissionAndPicImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY);
            } else {
                ImagePicker.pickImage(SignUpActivity.this);
            }
        } else {
            ImagePicker.pickImage(SignUpActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constant.SELECT_FILE);
                } else {
                    Toast.makeText(SignUpActivity.this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(SignUpActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(SignUpActivity.this);
                } else {
                    Toast.makeText(SignUpActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 234) {
                Uri imageUri = ImagePicker.getImageURIFromResult(SignUpActivity.this, requestCode, resultCode, data);
                bitmap = ImagePicker.getImageResized(SignUpActivity.this, imageUri);
                profile_image.setImageBitmap(bitmap);
            }
        }
    }


    public boolean isValidData() {
        Validation v = new Validation();

        if (bitmap == null) {
            Utils.openAlertDialog(SignUpActivity.this, "Please select profile image");
            return false;
        } else if (!v.isNullValue(sign_in_name.getText().toString().trim())) {
            Utils.openAlertDialog(SignUpActivity.this, getResources().getString(R.string.alert_name_null));

            return false;
        } else if (!v.isNullValue(sign_in_email.getText().toString().trim())) {
            Utils.openAlertDialog(SignUpActivity.this, getResources().getString(R.string.alert_email_null));

            return false;
        } else if (!v.isEmailValid(sign_in_email.getText().toString().trim())) {
            Utils.openAlertDialog(SignUpActivity.this, getResources().getString(R.string.alert_invalid_email));
            return false;
        } else if (!v.isNullValue(sign_in_contact.getText().toString().trim())) {
            Utils.openAlertDialog(SignUpActivity.this, "Please enter mobile number");
            return false;
        } else if (!v.isNullValue(sign_in_password.getText().toString().trim())) {
            Utils.openAlertDialog(SignUpActivity.this, getResources().getString(R.string.alert_password_null));

            return false;
        } else if (!v.isPasswordValid(sign_in_password.getText().toString().trim())) {
            Utils.openAlertDialog(SignUpActivity.this, getResources().getString(R.string.alert_invalid_password));
            return false;
        }

        return true;
    }

    private void signUpTask() {
        signUpBtn.setEnabled(false);
        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("fullName", sign_in_name.getText().toString().trim());
        map.put("email", sign_in_email.getText().toString().trim());
        map.put("countryCode", "+91");
        map.put("contactNumber", sign_in_contact.getText().toString().trim());
        map.put("password", sign_in_password.getText().toString().trim());
        map.put("deviceToken", "youwillgetsoon");
        map.put("userType", "2");
        map.put("deviceType", "1");
        map.put("socialId", "");
        map.put("socialType", "");

        Map<String, Bitmap> image = new HashMap<>();
        image.put("profileImage", bitmap);

        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                //loadingView.setVisibility(View.GONE);
                Log.e("SIGN IN RESPONSE", response);
                signUpBtn.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {
                        Gson gson = new Gson();
                        SignInInfo signInInfo = gson.fromJson(String.valueOf(jsonObject), SignInInfo.class);
                        session.createSession(signInInfo);

                        startActivity(new Intent(SignUpActivity.this, AdminHomeActivity.class));
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        }
                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Utils.openAlertDialog(SignUpActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    signUpBtn.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                Utils.openAlertDialog(SignUpActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
                signUpBtn.setEnabled(true);
            }
        });
        service.callMultiPartApi("userLogin", map, image);

    }

}
