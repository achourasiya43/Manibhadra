package com.manibhadra.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.manibhadra.R;
import com.manibhadra.activity.comman.SignInActivity;
import com.manibhadra.model.ProductDetailsInfo;
import com.manibhadra.model.SignInInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anil on 30-07-2018.
 */

public class SessionManager  {

    private Context context;
    private Activity activity;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static SharedPreferences sharedPreferences1;
    private static SharedPreferences.Editor editor1;

    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String IS_FIrebaseLogin = "isFirebaseLogin";
    private static final String PROFILEINFO = "profileInfo";
    private static final String FILTERINFO = "filter_info";
    private static final String USER_INTEREST_LIST = "user_interst_list";
    private static final String FILTER_LIST = "filter_list";
    private static final String SAVELATLNG = "savelatlng";
    private static final String PASSWORD = "password";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
        editor1 = sharedPreferences1.edit();
    }

    public void createSession(SignInInfo signInInfo) {
        createSession(signInInfo, false);
    }

    public void createSession(SignInInfo signInInfo, boolean isFirebaseLogin) {
        Gson gson = new Gson();
        String json = gson.toJson(signInInfo); // myObject - instance of MyObject
        editor.putString("signInInfo", json);
        editor.putBoolean(IS_LOGGEDIN, true);
        editor.putBoolean(IS_FIrebaseLogin, isFirebaseLogin);
        editor.putString("authToken", signInInfo.userDetail.authToken);
        editor.commit();
    }

    public SignInInfo getUser() {
        Gson gson = new Gson();
        String string = sharedPreferences.getString("signInInfo", "");
        return gson.fromJson(string, SignInInfo.class);
    }

    public void logout() {
        editor.clear();
        editor.apply();

        if(context != null){
            context.startActivity(new Intent(context, SignInActivity.class));
            ((Activity)context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            ((Activity)context).finish();
        }
    }

    public void savePassword(String password) {
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getPassword(){
        return sharedPreferences.getString(PASSWORD,"");
    }


    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGEDIN, false);
    }

    public String getAuthToken() {
        return sharedPreferences.getString("authToken", "");
    }

    public void savecardList(ArrayList<ProductDetailsInfo.ProductDetailBean> interestInfoList) {
        Gson gson = new Gson();
        String json = gson.toJson(interestInfoList);
        editor1.putString(USER_INTEREST_LIST, json);
        editor1.commit();
    }

    public List<ProductDetailsInfo.ProductDetailBean> getsavecardList(){
        Gson gson = new Gson();
        String str = sharedPreferences1.getString(USER_INTEREST_LIST,"");
        if (!str.isEmpty()){

            ProductDetailsInfo.ProductDetailBean[] interestInfo = gson.fromJson(str, ProductDetailsInfo.ProductDetailBean[].class);
            List<ProductDetailsInfo.ProductDetailBean> interestInfos = Arrays.asList(interestInfo);
            return  interestInfos;
        }
        else return null;
    }
}
