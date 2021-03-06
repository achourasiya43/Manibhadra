package com.manibhadra.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.manibhadra.ImagePickerPackge.ImagePicker;
import com.manibhadra.R;
import com.manibhadra.activity.customer.MainActivity;
import com.manibhadra.adapter.AddProductItemAdapter;
import com.manibhadra.adapter.DetailsProductAdapter;
import com.manibhadra.app.App;
import com.manibhadra.helper.Constant;
import com.manibhadra.helper.Validation;
import com.manibhadra.listner.GetPosition;
import com.manibhadra.listner.ListnerClass;
import com.manibhadra.model.ProductDetailsInfo;
import com.manibhadra.serverTask.Utils;
import com.manibhadra.serverTask.WebService;
import com.manibhadra.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView back, iv_card_update,iv_card_edit;
    private String product_key = "";
    private TextView action_bar_title,tv_notes,tv_notes_text;
    private Button addProductBtn;
    private LinearLayout details_view_layout, add_view_layout;
    private ImageView add_product_image, product_details_image;
    private Bitmap bitmap;
    private String productId = "";
    private String categoryId = "";
    private String userType = "";
    private ProgressBar progress_bar;
    private ProductDetailsInfo productDetailsInfo;
    private ArrayList<ProductDetailsInfo.ProductDetailBean> cardProductList;
    private ArrayList<ProductDetailsInfo.AddProduct> addProducts;
    private SessionManager sessionManager;
    private AddProductItemAdapter productItemAdapter;
    private DetailsProductAdapter detailsProductAdapter;
    private RecyclerView recycler_view, recycler_view_details;
    private ImageView add_item_in_card;
    private EditText ed_size, ed_color, ed_rate;
    private ProductDetailsInfo.ProductDetailBean productDetailsInfoCardData;

    //Add To Cart
    ArrayList<ProductDetailsInfo.ProductDetailBean> cardItemList;

    /*...........add product.............*/
    EditText ed_product_name, ed_product_details, ed_product_other_details;

    /*...........product details ......*/
    TextView tv_product_name, tv_product_details, tv_other_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        /*.......comman..............*/
        progress_bar = findViewById(R.id.progress_bar);
        addProductBtn = findViewById(R.id.addProductBtn);
        back = findViewById(R.id.back);
        action_bar_title = findViewById(R.id.action_bar_title);

        /*.............for details..........*/
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_product_details = findViewById(R.id.tv_product_details);
        tv_other_details = findViewById(R.id.tv_other_details);
        product_details_image = findViewById(R.id.product_details_image);
        details_view_layout = findViewById(R.id.details_view_layout);
        recycler_view_details = findViewById(R.id.recycler_view_details);

        // for add product
        add_product_image = findViewById(R.id.add_product_image);
        add_view_layout = findViewById(R.id.add_view_layout);
        ed_product_name = findViewById(R.id.ed_product_name);
        ed_product_details = findViewById(R.id.ed_product_details);
        ed_product_other_details = findViewById(R.id.ed_product_other_details);
        recycler_view = findViewById(R.id.recycler_view);
        add_item_in_card = findViewById(R.id.add_item_in_card);
        ed_size = findViewById(R.id.ed_size);
        ed_color = findViewById(R.id.ed_color);
        ed_rate = findViewById(R.id.ed_rate);

        //for card details
        iv_card_update = findViewById(R.id.iv_card_update);
        iv_card_edit = findViewById(R.id.iv_card_edit);
        tv_notes_text = findViewById(R.id.tv_notes_text);
        tv_notes = findViewById(R.id.tv_notes);

        cardProductList = new ArrayList<>();
        addProducts = new ArrayList<>();
        sessionManager = new SessionManager(this);

        productItemAdapter = new AddProductItemAdapter(addProducts, this, new ListnerClass() {
            @Override
            public void getPosition(int position) {
                updateRowDialog(position);
            }
        });
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(productItemAdapter);

        if (getIntent().getStringExtra("product_key") != null) {
            product_key = getIntent().getStringExtra("product_key");
            productId = getIntent().getStringExtra("productId");
            userType = getIntent().getStringExtra("userType");

            if(userType.equals("updateProduct")){
                categoryId = getIntent().getStringExtra("categoryId");
                add_view_layout.setVisibility(View.VISIBLE);
                details_view_layout.setVisibility(View.GONE);
                action_bar_title.setText("Update Product");
                addProductBtn.setText("Update Product");
                productdetails();
            }
            else if (userType.equals("custmer")) {
                action_bar_title.setText("Product Details");
                addProductBtn.setText("Add To Cart");

                add_view_layout.setVisibility(View.GONE);
                details_view_layout.setVisibility(View.VISIBLE);
            }
            else if (product_key.equals("viewProduct")) {
                action_bar_title.setText("Product Details");
                addProductBtn.setText("Delete Product");

                add_view_layout.setVisibility(View.GONE);
                details_view_layout.setVisibility(View.VISIBLE);

            } else if (product_key.equals("addProduct")) {
                categoryId = getIntent().getStringExtra("categoryId");
                action_bar_title.setText("Add Product");

                add_view_layout.setVisibility(View.VISIBLE);
                details_view_layout.setVisibility(View.GONE);

            } else if (product_key.equals("cardDetailsView")) {
                iv_card_edit.setVisibility(View.VISIBLE);
                cardItemList = (ArrayList<ProductDetailsInfo.ProductDetailBean>) getIntent().getSerializableExtra("cardItemList");
                int position = getIntent().getIntExtra("position", 0);
                productDetailsInfoCardData = cardItemList.get(position);
                action_bar_title.setText("Card Details");
                addProductBtn.setText("");
                viewCardDetails(productDetailsInfoCardData);
                add_view_layout.setVisibility(View.GONE);
                details_view_layout.setVisibility(View.VISIBLE);
                addProductBtn.setVisibility(View.GONE);
                tv_notes.setVisibility(View.VISIBLE);
                tv_notes_text.setVisibility(View.VISIBLE);
                tv_notes.setText(cardItemList.get(position).note);
            }
        }
        iv_card_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_card_update.setVisibility(View.GONE);
                iv_card_edit.setVisibility(View.VISIBLE);
                detailsProductAdapter.getUpdate("upDateCard");

                ArrayList<ProductDetailsInfo.AddProduct> ProductArrayList = detailsProductAdapter.getProductArrayList();
                Gson gson = new Gson();
                String json = gson.toJson(ProductArrayList);

                productDetailsInfoCardData.productData = json;
                sessionManager.savecardList(cardItemList);
            }
        });
        iv_card_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_card_edit.setVisibility(View.GONE);
                iv_card_update.setVisibility(View.VISIBLE);
                detailsProductAdapter.getUpdate("editCard");
            }
        });

        detailsProductAdapter = new DetailsProductAdapter(this, addProducts, userType, new GetPosition() {
            @Override
            public void getCardItemPosition(int position) {
                removetDialog(ProductDetailsActivity.this,"Do you want to remove?",position);
            }
        });
        recycler_view_details.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_details.setAdapter(detailsProductAdapter);

        add_item_in_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = ed_size.getText().toString().trim();
                String color = ed_color.getText().toString().trim();
                String rate = ed_rate.getText().toString().trim();

                if (!TextUtils.isEmpty(size) &&
                        !TextUtils.isEmpty(color) &&
                        !TextUtils.isEmpty(rate)) {


                    ProductDetailsInfo.AddProduct product = new ProductDetailsInfo.AddProduct();
                    product.productColors = color;
                    product.productRates = rate;
                    product.productSizes = size;

                    addProducts.add(product);
                    productItemAdapter.notifyDataSetChanged();

                    ed_size.setText("");
                    ed_color.setText("");
                    ed_rate.setText("");

                    ed_size.requestFocus();
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionAndPicImage();
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addProductBtn.getText().toString().trim().equals("Delete Product")) {
                    deleteSuccessDialog(ProductDetailsActivity.this,"Do you want to delete ?");

                } else if (addProductBtn.getText().toString().trim().equals("Add Product") ||
                        addProductBtn.getText().toString().trim().equals("Update Product")) {
                    if (isValidData()) {
                        if (addProducts.size() != 0) {
                            addProducts();
                        } else {
                            Utils.openAlertDialog(ProductDetailsActivity.this, "Please add product sizes,colors and rates");
                        }
                    }
                } else if (addProductBtn.getText().toString().trim().equals("Add To Cart")) {
                    boolean isProductItemSelected = false;

                    for (int i = 0; i < addProducts.size(); i++)
                        if (addProducts.get(i).isChecked)
                            isProductItemSelected = true;

                    if (isProductItemSelected) {
                        addToCardDialog();
                    } else
                        Utils.openAlertDialog(ProductDetailsActivity.this, "Please select product");
                }
            }
        });

        if (product_key.equals("viewProduct")) {
            productdetails();
        }
    }

    @Override
    public void onBackPressed() {

        if (userType.equals("custmer")){
            boolean isProductItemSelected = false;
            for (int i = 0; i < addProducts.size(); i++)
                if (addProducts.get(i).isChecked)
                    isProductItemSelected = true;

            if (isProductItemSelected) {
                addToCardDialog();
            }else {
                super.onBackPressed();
            }
        }else
            super.onBackPressed();
    }

    public void removetDialog(Context context, String message, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addProducts.remove(position);
                ArrayList<ProductDetailsInfo.AddProduct> ProductArrayList = addProducts;
                Gson gson = new Gson();
                String json = gson.toJson(ProductArrayList);
                productDetailsInfoCardData.productData = json;

                sessionManager.savecardList(cardItemList);
                detailsProductAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if(!activity.isFinishing())
            alert.show();
    }

    public void deleteSuccessDialog(Context context, String message) {
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
                deleteproduct();
            }
        });
        AlertDialog alert = builder.create();
        Activity activity = (Activity) context;
        if(!activity.isFinishing())
            alert.show();
    }

    private void viewCardDetails(ProductDetailsInfo.ProductDetailBean productDetailsInfo) {
        JSONArray array = null;
        try {
            array = new JSONArray(productDetailsInfo.productData);
            for (int i = 0; i < array.length(); i++) {
                ProductDetailsInfo.AddProduct product = new ProductDetailsInfo.AddProduct();
                product.productSizes = array.getJSONObject(i).getString("productSizes");
                product.productColors = array.getJSONObject(i).getString("productColors");
                product.productRates = array.getJSONObject(i).getString("productRates");
                product.productQty = array.getJSONObject(i).getString("productQty");
                product.isChecked = array.getJSONObject(i).getBoolean("isChecked");

                if (!product.productSizes.equals("") &&
                        !product.productColors.equals("") &&
                        !product.productRates.equals("")) {

                    addProducts.add(product);
                }
            }
            setDetaislData(productDetailsInfo);
            detailsProductAdapter = new DetailsProductAdapter(this, addProducts, userType, new GetPosition() {
                @Override
                public void getCardItemPosition(int position) {}// there is no need to call
            });
            recycler_view_details.setLayoutManager(new LinearLayoutManager(this));
            recycler_view_details.setAdapter(detailsProductAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean isValidData() {
        Validation v = new Validation();

        if (!v.isNullValue(ed_product_name.getText().toString().trim())) {
            Utils.openAlertDialog(ProductDetailsActivity.this, "Please enter product name");
            return false;
        }

        return true;
    }

    /*...........edit product dialog*/
    private void updateRowDialog(final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.row_update);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button add_done_btn = dialog.findViewById(R.id.add_done_btn);
        final EditText ed_size = dialog.findViewById(R.id.ed_size);
        final EditText ed_color = dialog.findViewById(R.id.ed_color);
        final EditText ed_rate = dialog.findViewById(R.id.ed_rate);
        ImageView close_button = dialog.findViewById(R.id.close_button);

        ed_size.setText(addProducts.get(position).productSizes);
        ed_color.setText(addProducts.get(position).productColors);
        ed_rate.setText(addProducts.get(position).productRates);

        add_done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = ed_size.getText().toString().trim();
                String color = ed_color.getText().toString().trim();
                String rate = ed_rate.getText().toString().trim();

                if(!TextUtils.isEmpty(size) &&
                        !TextUtils.isEmpty(color) &&
                        !TextUtils.isEmpty(rate)){
                    ProductDetailsInfo.AddProduct product = new ProductDetailsInfo.AddProduct();
                    product.productSizes = size;
                    product.productRates  = rate;
                    product.productColors = color;
                    addProducts.set(position,product);
                    productItemAdapter.notifyDataSetChanged();

                    Utils.hideKeyboard(ProductDetailsActivity.this);
                    dialog.dismiss();
                }
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    /*...............productdetails.................*/

    private void productdetails() {
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
                    addProducts.clear();
                    if (status.equals("success")) {
                        Gson gson = new Gson();
                        productDetailsInfo = gson.fromJson(response, ProductDetailsInfo.class);
                        String data = productDetailsInfo.productDetail.productData;
                        JSONArray array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            productDetailsInfo.addProduct = new ProductDetailsInfo.AddProduct();
                            productDetailsInfo.addProduct.productSizes = array.getJSONObject(i).getString("productSizes");
                            productDetailsInfo.addProduct.productColors = array.getJSONObject(i).getString("productColors");
                            productDetailsInfo.addProduct.productRates = array.getJSONObject(i).getString("productRates");

                            /*if (!productDetailsInfo.addProduct.productSizes.equals("") &&
                                    !productDetailsInfo.addProduct.productColors.equals("") &&
                                    !productDetailsInfo.addProduct.productRates.equals("")) {


                            }*/

                            addProducts.add(productDetailsInfo.addProduct);
                        }
                        setDetaislData(productDetailsInfo.productDetail);

                    } else {
                        Utils.openAlertDialog(ProductDetailsActivity.this, message);
                    }
                    detailsProductAdapter.notifyDataSetChanged();
                    productItemAdapter.notifyDataSetChanged();

                    progress_bar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                //Utils.openAlertDialog(ProductDetailsActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
            }
        });
        service.callGetSimpleVolley("user/getProductDetail?productId=" + productId + "");
    }

    private void setDetaislData(final ProductDetailsInfo.ProductDetailBean productDetailsInfo) {
        tv_product_name.setText(productDetailsInfo.productName);
        tv_product_details.setText(productDetailsInfo.productDetail);
        tv_other_details.setText(productDetailsInfo.productOtherDetail);
        Glide.with(ProductDetailsActivity.this).load(productDetailsInfo.productImage)
                .apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into(product_details_image);


        product_details_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.full_screen_photo_dialog(ProductDetailsActivity.this, productDetailsInfo.productImage);
            }
        });

        if(userType.equals("updateProduct")){
            ed_product_name.setText(productDetailsInfo.productName);
            ed_product_details.setText(productDetailsInfo.productDetail);
            ed_product_other_details.setText(productDetailsInfo.productOtherDetail);
            Glide.with(ProductDetailsActivity.this).load(productDetailsInfo.productImage)
                    .apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into(add_product_image);

        }

    }
/*.....................end details product............................................*/


/*...............delete product................................*/

    private void deleteproduct() {
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

                    if (status.equals("success")) {
                        successDeleteDialog(ProductDetailsActivity.this, message);
                    } else {
                        Utils.openAlertDialog(ProductDetailsActivity.this, message);
                    }
                    progress_bar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                //Utils.openAlertDialog(ProductDetailsActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
            }
        });
        service.callGetSimpleVolley("user/deleteProduct?productId=" + productId + "");
    }

    public void successDeleteDialog(Context context, String message) {
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
/*..............end delete product ............................*/


/*..........................getting Images code.............................*/

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

                if(userType.equals("updateProduct")){
                  add_product_image.setImageBitmap(bitmap);
                }else add_product_image.setImageBitmap(bitmap);
            }
        }
    }
/*.................end........................*/

/*..............Add product Api Calling .......................*/

    private void addProducts() {
        addProductBtn.setEnabled(false);
        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();

        if(userType.equals("updateProduct")){
            map.put("productId", productId);
        }
        /*............................................*/
        map.put("categoryId", categoryId);
        map.put("productName", ed_product_name.getText().toString().trim());
        map.put("productDetail", ed_product_details.getText().toString().trim());
        map.put("productOtherDetail", ed_product_other_details.getText().toString().trim());


        Gson gson = new Gson();
        String addProducts_json = gson.toJson(addProducts);
        map.put("productData", addProducts_json);

        Map<String, Bitmap> image;
        if (bitmap != null) {
            image = new HashMap<>();
            image.put("productImage", bitmap);
        } else {
            image = null;
        }


        WebService service = new WebService(this, App.TAG, new WebService.LoginRegistrationListener() {

            @Override
            public void onResponse(String response) {
                //loadingView.setVisibility(View.GONE);
                Log.e("SIGN IN RESPONSE", response);
                addProductBtn.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {
                        successAddProductDialog(ProductDetailsActivity.this, message);
                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Utils.openAlertDialog(ProductDetailsActivity.this, message);
                        addProducts.clear();
                        productItemAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    addProductBtn.setEnabled(true);
                    progress_bar.setVisibility(View.GONE);
                    addProducts.clear();
                    productItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void ErrorListener(VolleyError error) {
                //Utils.openAlertDialog(ProductDetailsActivity.this, "Something went wrong...");
                progress_bar.setVisibility(View.GONE);
                addProductBtn.setEnabled(true);
                addProducts.clear();
                productItemAdapter.notifyDataSetChanged();
            }
        });


        if(userType.equals("updateProduct")){
            service.callMultiPartApi("user/updateProduct", map, image);
        }else {
            service.callMultiPartApi("user/addProduct", map, image);
        }

    }

    public void successAddProductDialog(Context context, String message) {
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
    
    /*...............dialog Add To Cart..............*/

    private void addToCardDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.add_to_card_dialog_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button add_card_btn = dialog.findViewById(R.id.add_card_btn);
        final TextView dialog_header = dialog.findViewById(R.id.dialog_header);
        dialog_header.setText("Add To Cart");
        final RelativeLayout ly_qyt = dialog.findViewById(R.id.ly_qyt);
        ly_qyt.setVisibility(View.GONE);
        final EditText ed_note = dialog.findViewById(R.id.ed_note);
        ImageView close_button = dialog.findViewById(R.id.close_button);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        add_card_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 /*............................................................................................................*/                     //geting arraylist from session
                List<ProductDetailsInfo.ProductDetailBean> getproductDetailsList = sessionManager.getsavecardList();
                if (getproductDetailsList != null)
                    cardProductList.addAll(getproductDetailsList);
 /*.................................................................................................................*/

                productDetailsInfo.productDetail.note = ed_note.getText().toString().trim();

                ArrayList<ProductDetailsInfo.AddProduct> tempList = new ArrayList<>();
                for (int i = 0; i < addProducts.size(); i++) {
                    if (addProducts.get(i).isChecked) {
                        Gson gson = new Gson();
                        tempList.add(addProducts.get(i));
                        productDetailsInfo.productDetail.productData = gson.toJson(tempList);
                    }
                }
                cardProductList.add(productDetailsInfo.productDetail);

                sessionManager.savecardList(cardProductList);

                dialog.dismiss();
                finish();
            }

        });
        dialog.show();

    }
}
