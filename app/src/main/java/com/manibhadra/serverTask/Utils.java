package com.manibhadra.serverTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.manibhadra.R;

/**
 * Created by Anil on 01-08-2018.
 */

public class Utils {
    private static boolean connected = false;


    public static Boolean IsNetPresent(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }

    public static void openAlertDialog(Context context, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Manibhadra");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    public static void full_screen_photo_dialog(Context context ,String image_url){
        final Dialog openDialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        openDialog.setContentView(R.layout.full_image_view_dialog);
        ImageView iv_back = openDialog.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog.dismiss();
            }
        });

        PhotoView photoView = openDialog.findViewById(R.id.photo_view);
        if(!image_url.equals("")){
            Glide.with(context).load(image_url).apply(new RequestOptions().placeholder(R.drawable.ico_user_placeholder)).into(photoView);
        }
        openDialog.show();

    }
}


