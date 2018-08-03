package com.manibhadra.activity.admin;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manibhadra.ImagePickerPackge.ImagePicker;
import com.manibhadra.R;
import com.manibhadra.helper.Constant;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView back;
    String product_key;
    TextView action_bar_title;
    Button addProductBtn;
    LinearLayout details_view_layout,add_view_layout;
    ImageView product_image;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        add_view_layout = findViewById(R.id.add_view_layout);
        details_view_layout = findViewById(R.id.details_view_layout);

        addProductBtn = findViewById(R.id.addProductBtn);
        back = findViewById(R.id.back);
        action_bar_title = findViewById(R.id.action_bar_title);

        // for add product
        product_image = findViewById(R.id.product_image);

        if(getIntent().getStringExtra("product_key") != null){
            product_key = getIntent().getStringExtra("product_key");
            if(product_key.equals("viewProduct")){
                action_bar_title.setText("Product Details");
                addProductBtn.setText("Delete Product");

                add_view_layout.setVisibility(View.GONE);
                details_view_layout.setVisibility(View.VISIBLE);
            }
            else if(product_key.equals("addProduct")){
                action_bar_title.setText("Add Product");

                add_view_layout.setVisibility(View.VISIBLE);
                details_view_layout.setVisibility(View.GONE);

            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionAndPicImage();
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
                ImagePicker.pickImage(ProductDetailsActivity.this);
            }
        } else {
            ImagePicker.pickImage(ProductDetailsActivity.this);
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
                    Toast.makeText(ProductDetailsActivity.this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CEMERA_OR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(ProductDetailsActivity.this);
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
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
                Uri imageUri = ImagePicker.getImageURIFromResult(ProductDetailsActivity.this, requestCode, resultCode, data);
                bitmap = ImagePicker.getImageResized(ProductDetailsActivity.this, imageUri);
                product_image.setImageBitmap(bitmap);
            }
        }
    }
}
