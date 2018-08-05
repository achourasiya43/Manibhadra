package com.manibhadra.activity.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.manibhadra.ImagePickerPackge.ImagePicker;
import com.manibhadra.R;
import com.manibhadra.activity.SignUpActivity;
import com.manibhadra.app.App;
import com.manibhadra.helper.Constant;
import com.manibhadra.helper.Validation;
import com.manibhadra.model.SignInInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {
    private ImageView back;
    private Bitmap bitmap;
    private ImageView category_image;
    private Button add_category_Btn;
    private EditText ed_category;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        back = findViewById(R.id.back);
        category_image = findViewById(R.id.category_image);
        add_category_Btn = findViewById(R.id.add_category_Btn);
        ed_category = findViewById(R.id.ed_category);
        progress_bar = findViewById(R.id.progress_bar);

        category_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPermissionAndPicImage();
            }
        });

        add_category_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData()){
                    addCategoryApi();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public boolean isValidData() {
        Validation v = new Validation();

        if (!v.isNullValue(ed_category.getText().toString().trim())) {
            Utils.openAlertDialog(AddCategoryActivity.this, "Please Add Category Name");
            return false;
        }

        return true;
    }

    public void getPermissionAndPicImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY);
            } else {
                ImagePicker.pickImage(AddCategoryActivity.this);
            }
        } else {
            ImagePicker.pickImage(AddCategoryActivity.this);
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
                    Toast.makeText(AddCategoryActivity.this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(AddCategoryActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(AddCategoryActivity.this);
                } else {
                    Toast.makeText(AddCategoryActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
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
                Uri imageUri = ImagePicker.getImageURIFromResult(AddCategoryActivity.this, requestCode, resultCode, data);
                bitmap = ImagePicker.getImageResized(AddCategoryActivity.this, imageUri);
                category_image.setImageBitmap(bitmap);
            }
        }
    }

    private void addCategoryApi() {
        add_category_Btn.setEnabled(false);
        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("categoryName", ed_category.getText().toString().trim());

        Map<String, Bitmap> image ;
        if(bitmap != null){
            image = new HashMap<>();
            image.put("categoryImage", bitmap);
        }else {
            image = null;
        }


        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                //loadingView.setVisibility(View.GONE);
                Log.e("SIGN IN RESPONSE", response);
                add_category_Btn.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {
                        successDialog(AddCategoryActivity.this,message);

                    } else {
                        Utils.openAlertDialog(AddCategoryActivity.this, message);
                    }
                    progress_bar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    add_category_Btn.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                Utils.openAlertDialog(AddCategoryActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
                add_category_Btn.setEnabled(true);
            }
        });
        service.callMultiPartApi("user/addCategory", map, image);

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
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
